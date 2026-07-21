package com.example.flexsim_simulation_service.controller.flexsim;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentProgressEventDto;
import com.example.flexsim_simulation_service.DTO.experiment.ExperimentRunProgressDto;
import com.example.flexsim_simulation_service.DTO.flexsim.FlexsimStatusUpdateDto;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.service.experiment.ExperimentProgressSseService;
import com.example.flexsim_simulation_service.service.experiment.ExperimentRunProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
