package com.postnord.simulation_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ExperimentProgressEventDto;
import com.postnord.simulation_service.dto.ExperimentRunProgressDto;
import com.postnord.simulation_service.dto.FlexsimStatusUpdateDto;
import com.postnord.simulation_service.service.ExperimentProgressSseService;
import com.postnord.simulation_service.service.ExperimentRunProgressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flexsim")
public class FlexsimSimulationController {

    private final ExperimentRunProgressService experimentRunProgressService;

    private final ExperimentProgressSseService experimentProgressSseService;

    @PostMapping("/updateSimulationStage")
    public ResponseEntity<String> updateSimulationStage(@RequestBody FlexsimStatusUpdateDto flexsimStatusUpdateDto)
    {
        experimentRunProgressService.updateStage(
                flexsimStatusUpdateDto.getExperimentId(),
                flexsimStatusUpdateDto.getExperimentRunStage(),
                flexsimStatusUpdateDto.getMessage()
        );

        experimentProgressSseService.send(
                flexsimStatusUpdateDto.getExperimentId(),
                ExperimentProgressEventDto.builder()
                        .experimentId(flexsimStatusUpdateDto.getExperimentId())
                        .status("RUNNING")
                        .stage(flexsimStatusUpdateDto.getExperimentRunStage().name())
                        .message(flexsimStatusUpdateDto.getMessage())
                        .build()
        );

        return ResponseEntity.ok("Simulation stage updated successfully");
    }

    @GetMapping("/getSimulationStatus/{experimentId}")
    public ResponseEntity<ExperimentRunProgressDto> getStatus(@PathVariable Long experimentId) {
        return ResponseEntity.ok(experimentRunProgressService.getLatestProgress(experimentId));
    }

}
