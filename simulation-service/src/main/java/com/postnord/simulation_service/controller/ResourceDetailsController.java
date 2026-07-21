package com.postnord.simulation_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ExperimentProgressEventDto;
import com.postnord.simulation_service.dto.FlexsimInfeedResourceDetailDto;
import com.postnord.simulation_service.dto.FlexsimZoneResourceDetailDto;
import com.postnord.simulation_service.dto.InfeedResourceDetailDto;
import com.postnord.simulation_service.dto.ZoneResourceDetailDto;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.service.ExperimentProgressSseService;
import com.postnord.simulation_service.service.ExperimentRunProgressService;
import com.postnord.simulation_service.service.InfeedResourceDetailService;
import com.postnord.simulation_service.service.ZoneResourceDetailService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resourceDetails")
public class ResourceDetailsController {
    private final InfeedResourceDetailService infeedResourceDetailService;

    private final ZoneResourceDetailService zoneResourceDetailService;

    private final ExperimentRunProgressService experimentRunProgressService;

    private final ExperimentProgressSseService experimentProgressSseService;


    @PostMapping("/setZoneDetails")
    public ResponseEntity<String> saveZoneResourceDetail(@RequestBody List<ZoneResourceDetailDto> zoneResourceDetailDtos)
    {
        return ResponseEntity.ok(zoneResourceDetailService.saveZoneResourceDetails(zoneResourceDetailDtos));
    }

    @GetMapping("/getZoneDetails/{exp_id}")
    public ResponseEntity<List<FlexsimZoneResourceDetailDto>> getZoneResourceDetails(@PathVariable("exp_id") Long experimentId){
        experimentRunProgressService.updateStage(
                experimentId,
                ExperimentRunStage.GETTING_ZONE_RESOURCE_DETAILS,
                "Importing Zone Resource Details");

        experimentProgressSseService.send(
                experimentId,
                ExperimentProgressEventDto.builder()
                        .experimentId(experimentId)
                        .status("RUNNING")
                        .stage(ExperimentRunStage.GETTING_ZONE_RESOURCE_DETAILS.name())
                        .message("Importing Zone Resource Details")
                        .build()
        );

        List<FlexsimZoneResourceDetailDto> zoneResourceDetailDtos = zoneResourceDetailService.getZoneResourceDetailsForExperiment(experimentId);

        return ResponseEntity.ok(zoneResourceDetailDtos);
    }

    @PostMapping("/setInfeedDetails")
    public ResponseEntity<String> saveInfeedResourceDetail(@RequestBody List<InfeedResourceDetailDto> infeedResourceDetailDtos)
    {
        return ResponseEntity.ok(infeedResourceDetailService.saveInfeedResourceDetails(infeedResourceDetailDtos));
    }

    @GetMapping("/getInfeedDetails/{exp_id}")
    public ResponseEntity<List<FlexsimInfeedResourceDetailDto>> getInfeedResourceDetails(@PathVariable("exp_id") Long experimentId){
        experimentRunProgressService.updateStage(
                experimentId,
                ExperimentRunStage.GETTING_INFEED_RESOURCE_DETAILS,
                "Importing Infeed Resource Details");

        experimentProgressSseService.send(
                experimentId,
                ExperimentProgressEventDto.builder()
                        .experimentId(experimentId)
                        .status("RUNNING")
                        .stage(ExperimentRunStage.GETTING_INFEED_RESOURCE_DETAILS.name())
                        .message("Importing Infeed Resource Details")
                        .build()
        );

        List<FlexsimInfeedResourceDetailDto> infeedResourceDetailDtos = infeedResourceDetailService.getInfeedResourceDetailsForExperiment(experimentId);

         return ResponseEntity.ok(infeedResourceDetailDtos);
    }

}
