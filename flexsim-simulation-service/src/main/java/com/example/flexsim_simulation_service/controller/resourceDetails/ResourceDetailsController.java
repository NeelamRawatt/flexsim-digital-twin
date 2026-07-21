package com.example.flexsim_simulation_service.controller.resourceDetails;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentProgressEventDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimInfeedResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.FlexsimZoneResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.InfeedResourceDetailDto;
import com.example.flexsim_simulation_service.DTO.resourceDetails.ZoneResourceDetailDto;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.service.experiment.ExperimentProgressSseService;
import com.example.flexsim_simulation_service.service.experiment.ExperimentRunProgressService;
import com.example.flexsim_simulation_service.service.resourceDetails.InfeedResourceDetailService;
import com.example.flexsim_simulation_service.service.resourceDetails.ZoneResourceDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
