package com.postnord.simulation_service.consumer;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.postnord.simulation_service.config.FlexsimProperties;
import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.dto.ExperimentProgressEventDto;
import com.postnord.simulation_service.enums.ExperimentRunStage;
import com.postnord.simulation_service.event.SimulationTriggerEvent;
import com.postnord.simulation_service.producer.SimulationStatusProducer;
import com.postnord.simulation_service.service.DummyFlexsimRunner;
import com.postnord.simulation_service.service.ExperimentProgressSseService;
import com.postnord.simulation_service.service.ExperimentRunProgressService;
import com.postnord.simulation_service.service.FlexsimScriptService;
import com.postnord.simulation_service.util.FlexsimPathUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimulationTriggerConsumer {

    // This reads FlexSim-related configuration from application.properties.
    private final FlexsimProperties flexsimProperties;
    // It creates FlexSim script file.
    private final FlexsimScriptService flexsimScriptService;
    // This saves progress in DB.
    private final ExperimentRunProgressService experimentRunProgressService;
    // This sends live progress to frontend.
    private final ExperimentProgressSseService experimentProgressSseService;
    // This publishes status to Kafka topic:
    private final SimulationStatusProducer simulationStatusProducer;
    private final DummyFlexsimRunner dummyFlexsimRunner;

    // concurrency reads from application.properties -- change the number there, not here,
    // if the real machine's capacity is ever revised.
    @KafkaListener(
                topics = "${kafka.topics.simulation-trigger}",
                groupId = "simulation-service",
                concurrency = "${simulation.max-concurrent-runs}"
    )
    public void handle(SimulationTriggerEvent event) {
        Long experimentId = event.getExperimentId();
        String threadName = Thread.currentThread().getName(); // useful in logs, to actually see 4 different threads working

        log.info("[{}] Now processing queued simulation for experiment {}", threadName, experimentId);

        try {
            experimentRunProgressService.startRun(experimentId, ExperimentRunStage.OPENING_FLEXSIM, "Opening Flexsim");
            simulationStatusProducer.publish(experimentId, "RUNNING");
            experimentProgressSseService.send(experimentId, ExperimentProgressEventDto.builder()
                    .experimentId(experimentId).status("RUNNING")
                    .stage(ExperimentRunStage.OPENING_FLEXSIM.name()).message("Opening Flexsim").build());

            if ("dummy".equalsIgnoreCase(flexsimProperties.getMode())) {
                dummyFlexsimRunner.runSync(experimentId);
            } else {
                startRealFlexsim(event);
            }

            log.info("[{}] Finished processing experiment {} -- this thread is free for its next message", threadName, experimentId);

        } catch (Exception e) {
            // Catching everything here, in one place, for both dummy and real mode --
            // this is the fix for the gap we flagged earlier, where only real mode had error handling.
            log.error("[{}] Simulation failed for experiment {}: {}", threadName, experimentId, e.getMessage());
            experimentRunProgressService.markFailed(experimentId, e.getMessage());
            simulationStatusProducer.publish(experimentId, "FAILED");
        }
    }

    private void startRealFlexsim(SimulationTriggerEvent event) throws Exception {
        ExperimentDto dto = ExperimentDto.builder()
                .experimentId(event.getExperimentId())
                .selectedDate(event.getSelecteDate())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .maxRecirculationCount(event.getMaxRecirculationCount())
                .build();

        ProcessBuilder processBuilder = new ProcessBuilder(
                flexsimProperties.getPath(),
                FlexsimPathUtil.getFlexsimModelPath(),
                "/maintenance", "runscript", "/scriptpath",
                flexsimScriptService.createOrUpdateScript(dto)
        );
        Process process = processBuilder.start();
        process.waitFor(); // blocks only THIS one thread -- the other 3 partitions keep working independently
    
    
    
    
    
    }
}