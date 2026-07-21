package com.postnord.file_service.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;

import com.postnord.file_service.dto.FileValidationResponseDTO;

@Service
public class InfeedFileValidationService {
     private static  final Set<String> expected_tcs = new HashSet<>(Arrays.asList(
            "KIP1-KIP2",
            "KIP3-KIP4",
            "KIP5-KIP6",
            "KIP7-KIP8",
            "KIP9-KIP10",
            "B9-B10-B11",
            "B14-B15",
            "B113-B116"
    ));

    private static final Set<Integer> expected_shifts=new HashSet<>(Arrays.asList(
            1,2,3,4,5,6,7,8
    ));

    public FileValidationResponseDTO validateInfeedFile(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        Set<Integer> foundShiftIds = new HashSet<>();
        Map<Integer,Set<String>> shiftTCMap= new HashMap<>();

        if(file==null || file.isEmpty()) {
            return FileValidationResponseDTO.builder()
                    .valid(false)
                    .message("File is empty or missing")
                    .errors(Collections.singletonList("Please upload a valid infeed file"))
                    .build();

        }

        try(Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
                return FileValidationResponseDTO.builder()
                        .valid(false)
                        .message("Infeed file Validation failed")
                        .errors(Collections.singletonList("Uploaded file contains no data"))
                        .build();
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return FileValidationResponseDTO.builder()
                        .valid(false)
                        .message("Infeed file Validation failed")
                        .errors(Collections.singletonList("Header row is missing"))
                        .build();

            }

            int shiftIdCol = -1;
            int tcCol = -1;
            int noOfResource = -1;

            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue().trim();
                if ("Shift_ID".equalsIgnoreCase(header)) {
                    shiftIdCol = cell.getColumnIndex();
                } else if ("TC".equalsIgnoreCase(header)) {
                    tcCol = cell.getColumnIndex();
                } else if ("No of Resources".equalsIgnoreCase(header)) {
                    noOfResource = cell.getColumnIndex();
                }
            }

            if (shiftIdCol == -1 || tcCol == -1 || noOfResource == -1) {
                errors.add("REQUIRED COLUMN MISSING. 'SHIFT ID ' , 'TC' AND 'NO OF RESOURCES' COLUMNS MUST BE PRESENT");
                return FileValidationResponseDTO.builder()
                        .valid(false)
                        .message("Infeed file validation failed")
                        .errors(errors)
                        .build();
            }


            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null)
                    continue;

                DataFormatter dataFormatter = new DataFormatter();
                String tcValue = dataFormatter.formatCellValue(row.getCell(tcCol)).trim();
                String shiftValue = dataFormatter.formatCellValue(row.getCell(shiftIdCol)).trim();
                String resourceValue = dataFormatter.formatCellValue(row.getCell(noOfResource)).trim();

                Integer shiftId = null;
                boolean isValidRow=true;


                if (shiftValue.isEmpty()) {
                    errors.add("ShiftId is missing at row " + (i + 1));
                    isValidRow=false;
                } else {
                    try {
                        shiftId = Integer.parseInt(shiftValue);
                        foundShiftIds.add(shiftId);
                    } catch (NumberFormatException e) {
                        errors.add("Invalid shift Id" + shiftValue + "at row" + (i + 1));
                        isValidRow=false;
                    }
                }

                boolean isTcValid = tcValue.contains("-")
                        && expected_tcs.contains(tcValue);

                if (!isTcValid)
                {
                    errors.add("INVALID TC" + tcValue + "AT ROW " + (i + 1));
                    isValidRow=false;

                }



                if (isValidRow)
                {
                    shiftTCMap
                            .computeIfAbsent(shiftId,key->new HashSet<>())
                            .add(tcValue);
                }


            }

            //MISSING SHIFT IDS
            Set<Integer> missingShifts = new HashSet<>(expected_shifts);
            missingShifts.removeAll(foundShiftIds);

            if(!missingShifts.isEmpty()) {
                errors.add("MISSING SHIFT IDS : " + missingShifts);
            }

            for(Integer expectedShift : expected_shifts) {

                Set<String> tcsForShift = shiftTCMap.getOrDefault(expectedShift, Collections.emptySet());
                Set<String> missingTcsForShift = new HashSet<>(expected_tcs) ;
                for (String expectedTc : (expected_tcs)) {
                    missingTcsForShift.removeAll(tcsForShift);

                    if (!missingTcsForShift.isEmpty()) {

                        errors.add("FOR SHIFT ID " + expectedShift + "MISSING TC VALUES : " + missingTcsForShift);
                    }
                }
            }

            if(!errors.isEmpty())
            {
                return FileValidationResponseDTO.builder()
                        .valid(false)
                        .message("INFEED FILE VALIDATION FAILED")
                        .errors(errors)
                        .build();


            }

            return FileValidationResponseDTO.builder()
                    .valid(true)
                    .message("INFEED FILE IS VALID ")
                    .errors(Collections.emptyList())
                    .build();

        }
        catch (Exception e)
        {
            return FileValidationResponseDTO.builder()
                    .valid(false)
                    .message("Error while processing file ")
                    .errors(Collections.singletonList("System error : " + e.getMessage()))
                    .build();

        }
    }



}
