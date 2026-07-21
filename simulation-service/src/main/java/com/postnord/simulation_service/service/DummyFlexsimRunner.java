package com.postnord.simulation_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.postnord.simulation_service.config.FlexsimProperties;
import com.postnord.simulation_service.dto.ExperimentProgressEventDto;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.producer.SimulationStatusProducer;

@Component
@RequiredArgsConstructor
public class DummyFlexsimRunner {

    private final ExperimentRunProgressService experimentRunProgressService;
    private final ExperimentProgressSseService experimentProgressSseService;
    private final FlexsimProperties flexsimProperties;
    private final SimulationStatusProducer simulationStatusProducer;

    // @Async
    public void runSync(Long experimentId) {
        try {
            long delay = flexsimProperties.getDummyStageDelayMs();

            simulateStage(experimentId, ExperimentRunStage.IMPORTING_PARCEL_DATA, "Importing parcel data", delay);
            simulateStage(experimentId, ExperimentRunStage.GETTING_INFEED_RESOURCE_DETAILS, "Getting infeed resource details", delay);
            simulateStage(experimentId, ExperimentRunStage.GETTING_ZONE_RESOURCE_DETAILS, "Getting zone resource details", delay);
            simulateStage(experimentId, ExperimentRunStage.STARTING_SIMULATION, "Starting simulation", delay);
            simulateStage(experimentId, ExperimentRunStage.SIMULATION_STARTED, "Simulation running", delay);

            experimentRunProgressService.markCompleted(experimentId, "Dummy simulation completed");
            simulationStatusProducer.publish(experimentId, "COMPLETED"); // THE MISSING LINE

            
            experimentProgressSseService.send(experimentId, ExperimentProgressEventDto.builder()
                    .experimentId(experimentId).status("COMPLETED").stage(ExperimentRunStage.FINISHED.name())
                    .message("Dummy simulation completed").build());
            experimentProgressSseService.complete(experimentId);

        } catch (Exception e) {
            experimentRunProgressService.markFailed(experimentId, e.getMessage());
            simulationStatusProducer.publish(experimentId, "FAILED"); 
            
            experimentProgressSseService.send(experimentId, ExperimentProgressEventDto.builder()
                    .experimentId(experimentId).status("FAILED").stage("FAILED")
                    .message("Dummy simulation failed: " + e.getMessage()).build());
        }
    }

    private void simulateStage(Long experimentId, ExperimentRunStage stage, String message, long delayMs) throws InterruptedException {
        Thread.sleep(delayMs);
        experimentRunProgressService.updateStage(experimentId, stage, message);
        experimentProgressSseService.send(experimentId, ExperimentProgressEventDto.builder()
                .experimentId(experimentId).status("RUNNING").stage(stage.name()).message(message).build());
    }
}