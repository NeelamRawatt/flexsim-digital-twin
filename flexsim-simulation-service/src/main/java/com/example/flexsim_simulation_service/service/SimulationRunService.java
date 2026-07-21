package com.example.flexsim_simulation_service.service;


import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.DTO.experiment.ExperimentProgressEventDto;
import com.example.flexsim_simulation_service.config.FlexsimProperties;
import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import com.example.flexsim_simulation_service.repository.SimulationResultRepository;
import com.example.flexsim_simulation_service.service.experiment.ExperimentProgressSseService;
import com.example.flexsim_simulation_service.service.experiment.ExperimentRunProgressService;
import com.example.flexsim_simulation_service.util.FlexsimPathUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SimulationRunService {

    private final SimulationResultRepository simulationResultRepository;

    private final FlexsimProperties flexsimProperties;

    private final FlexsimScriptService flexsimScriptService;

    private final ExperimentRunProgressService experimentRunProgressService;

    private final ExperimentProgressSseService experimentProgressSseService;

    public ResponseEntity<String> startSimulation(ExperimentDto experimentDto) {

        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder(
                flexsimProperties.getPath(),
                FlexsimPathUtil.getFlexsimModelPath(),
                "/maintenance",
                "runscript",
                "/scriptpath",
                flexsimScriptService.createOrUpdateScript(experimentDto)
            );

            experimentRunProgressService.startRun(
                    experimentDto.getExperimentId(),
                    ExperimentRunStage.OPENING_FLEXSIM,
                    "Opening Flexsim");

            experimentProgressSseService.send(
                    experimentDto.getExperimentId(),
                    ExperimentProgressEventDto.builder()
                            .experimentId(experimentDto.getExperimentId())
                            .status("RUNNING")
                            .stage(ExperimentRunStage.OPENING_FLEXSIM.name())
                            .message("Opening Flexsim")
                            .build()
            );

            processBuilder.start();

            return ResponseEntity.ok("Simulation started succesfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to start simulation" + e.getMessage());
        }
    }



}
