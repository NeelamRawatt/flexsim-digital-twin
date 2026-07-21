package com.postnord.file_service.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.postnord.file_service.dto.FileValidationResponseDTO;
import org.apache.poi.ss.usermodel.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ZoneFileValidationService {
    
    private static final Set<Integer> EXPECTED_SHIFTS =

            IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toSet());

    private static final Set<String> EXPECTED_ZONES = Stream.of(

            "ZONE-1", "ZONE-2", "ZONE-3", "ZONE-4"

    ).collect(Collectors.toSet());

    private static final List<String> EXPECTED_HEADERS = Arrays.asList(

            "Shift ID", "Zone ID", "Resource", "Chute_ID"

    );

    private static final Map<String, List<RangeType>> ZONE_RULES = buildZoneRules();

    public FileValidationResponseDTO validateZoneResourceFile(MultipartFile file) {

        List<String> errors = new ArrayList<>();

        Map<Integer, Set<String>> shiftZoneMap = new HashMap<>();

        if (file == null || file.isEmpty()) {

            return buildResponse(false, "File is empty or missing",

                    Collections.singletonList("Please upload a valid zone resource file"));

        }

        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(file.getBytes()))) {

            Sheet sheet = workbook.getSheetAt(0);

            DataFormatter formatter = new DataFormatter();

            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {

                return buildResponse(false, "Validation failed",

                        Collections.singletonList("File contains no data"));

            }

            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {

                return buildResponse(false, "Validation failed",

                        Collections.singletonList("Header row missing"));

            }

            validateHeaders(headerRow, formatter, errors);

            int shiftCol = 0;

            int zoneCol = 1;

            int resourceCol = 2;

            int chuteCol = 3;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {

                    errors.add("Row " + (i + 1) + " is empty");

                    continue;

                }

                String shiftVal = formatter.formatCellValue(row.getCell(shiftCol)).trim();

                String zoneVal = formatter.formatCellValue(row.getCell(zoneCol)).trim();

                String resourceVal = formatter.formatCellValue(row.getCell(resourceCol)).trim();

                String chuteVal = formatter.formatCellValue(row.getCell(chuteCol)).trim();

                if (shiftVal.isEmpty() || zoneVal.isEmpty() || resourceVal.isEmpty() || chuteVal.isEmpty()) {

                    errors.add("Shift ID, Zone ID, Resource and Chute_ID are mandatory at row " + (i + 1));

                    continue;

                }

                Integer shiftId;

                try {

                    shiftId = Integer.parseInt(shiftVal);

                } catch (Exception e) {

                    errors.add("Invalid Shift ID '" + shiftVal + "' at row " + (i + 1));

                    continue;

                }

                if (!EXPECTED_ZONES.contains(zoneVal)) {

                    errors.add("Invalid Zone ID '" + zoneVal + "' at row " + (i + 1));

                    continue;

                }

                if (!resourceVal.matches("\\d+")) {

                    errors.add("Invalid Resource '" + resourceVal + "' at row " + (i + 1));

                    continue;

                }

                if (!isValidCommaSeparatedChutes(chuteVal)) {

                    errors.add("Invalid Chute_ID format at row " + (i + 1)

                            + ". Chute ids must be comma separated like [101,102,103]");

                    continue;

                }

                List<Integer> chuteIds = parseChuteIds(chuteVal);

                if (chuteIds.isEmpty()) {

                    errors.add("No chute ids found at row " + (i + 1));

                    continue;

                }

                List<String> chuteTypes = chuteIds.stream()

                        .map(chuteId -> getChuteType(zoneVal, chuteId))

                        .collect(Collectors.toList());

                List<Integer> invalidChutes = IntStream.range(0, chuteIds.size())

                        .filter(index -> chuteTypes.get(index) == null)

                        .mapToObj(chuteIds::get)

                        .collect(Collectors.toList());

                if (!invalidChutes.isEmpty()) {

                    errors.add("Invalid chute ids " + invalidChutes + " for " + zoneVal + " at row " + (i + 1));

                    continue;

                }

                Set<String> distinctTypes = new HashSet<>(chuteTypes);

                if (distinctTypes.size() > 1) {

                    errors.add("All chute ids in row " + (i + 1)

                            + " must belong to same chute type");

                    continue;

                }

                shiftZoneMap

                        .computeIfAbsent(shiftId, k -> new HashSet<>())

                        .add(zoneVal);

            }

            validateShiftZonePresence(shiftZoneMap, errors);

            return errors.isEmpty()

                    ? buildResponse(true, "Zone resource file is valid", Collections.emptyList())

                    : buildResponse(false, "Validation failed", errors);

        } catch (Exception e) {

            return buildResponse(false, "Error processing file",

                    Collections.singletonList(e.getMessage()));

        }

    }

    private void validateHeaders(Row headerRow, DataFormatter formatter, List<String> errors) {

        IntStream.range(0, EXPECTED_HEADERS.size())

                .forEach(i -> {

                    String actual = formatter.formatCellValue(headerRow.getCell(i)).trim();

                    if (!EXPECTED_HEADERS.get(i).equalsIgnoreCase(actual)) {

                        errors.add("Invalid column at position " + (i + 1)

                                + ". Expected '" + EXPECTED_HEADERS.get(i)

                                + "' but found '" + actual + "'");

                    }

                });

    }

    private void validateShiftZonePresence(Map<Integer, Set<String>> shiftZoneMap, List<String> errors) {

        Set<Integer> foundShifts = shiftZoneMap.keySet();

        Set<Integer> missingShifts = EXPECTED_SHIFTS.stream()

                .filter(shift -> !foundShifts.contains(shift))

                .collect(Collectors.toSet());

        if (!missingShifts.isEmpty()) {

            errors.add("Missing Shift IDs: " + missingShifts);

        }

        EXPECTED_SHIFTS.forEach(shift -> {

            Set<String> zones = shiftZoneMap.getOrDefault(shift, Collections.emptySet());

            Set<String> missingZones = EXPECTED_ZONES.stream()

                    .filter(zone -> !zones.contains(zone))

                    .collect(Collectors.toSet());

            if (!missingZones.isEmpty()) {

                errors.add("Shift " + shift + " missing Zones: " + missingZones);

            }

        });

    }

    private boolean isValidCommaSeparatedChutes(String value) {

        return value.matches("^\\[?\\d+(\\s*,\\s*\\d+)*\\]?$");

    }

    private List<Integer> parseChuteIds(String value) {

        String cleaned = value.replace("[", "").replace("]", "").trim();

        return Arrays.stream(cleaned.split(","))

                .map(String::trim)

                .filter(v -> !v.isEmpty())

                .map(Integer::parseInt)

                .collect(Collectors.toList());

    }

    private String getChuteType(String zoneId, Integer chuteId) {

        return ZONE_RULES.getOrDefault(zoneId, Collections.emptyList())

                .stream()

                .filter(rule -> rule.matches(chuteId))

                .map(RangeType::getType)

                .findFirst()

                .orElse(null);

    }

    private FileValidationResponseDTO buildResponse(boolean valid, String msg, List<String> errors) {

        return FileValidationResponseDTO.builder()

                .valid(valid)

                .message(msg)

                .errors(errors)

                .build();

    }

    private static Map<String, List<RangeType>> buildZoneRules() {

        Map<String, List<RangeType>> rules = new HashMap<>();

        rules.put("ZONE-1", Arrays.asList(

                new RangeType(252, 252, "Reject Chute")

        ));

        rules.put("ZONE-2", Arrays.asList(

                new RangeType(101, 114, "Spiral Chute"),

                new RangeType(121, 135, "Boom Conveyor Chute"),

                new RangeType(301, 337, "Direct Chute")

        ));

        rules.put("ZONE-3", Arrays.asList(

                new RangeType(152, 152, "Reject Chute")

        ));

        rules.put("ZONE-4", Arrays.asList(

                new RangeType(201, 214, "Spiral Chute"),

                new RangeType(221, 236, "Boom Conveyor Chute"),

                new RangeType(402, 433, "Direct Chute")

        ));

        return rules;

    }

    private static class RangeType {

        private final int start;

        private final int end;

        private final String type;

        public RangeType(int start, int end, String type) {

            this.start = start;

            this.end = end;

            this.type = type;

        }

        public boolean matches(int chuteId) {

            return chuteId >= start && chuteId <= end;

        }

        public String getType() {

            return type;

        }

    }

}
