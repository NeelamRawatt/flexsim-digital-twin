package com.postnord.experiment_service.consumer;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.postnord.experiment_service.event.SimulationStatusEvent;
import com.postnord.experiment_service.service.ExperimentService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimulationStatusConsumer {

    private final ExperimentService experimentService;

    @KafkaListener(topics = "${kafka.topics.simulation-status}", groupId = "experiment-service")
    public void handle(SimulationStatusEvent event) {
        log.info("Received: experiment {} is now {}", event.getExperimentId(), event.getStatus());
        experimentService.updateStatus(event.getExperimentId(), event.getStatus());
    }
}