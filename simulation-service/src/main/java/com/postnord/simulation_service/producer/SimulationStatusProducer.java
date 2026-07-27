package com.postnord.simulation_service.producer;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.postnord.simulation_service.event.SimulationStatusEvent;

@Slf4j
@Component
// This producer sends status updates to Kafka.
public class SimulationStatusProducer {

    private final KafkaTemplate<String, SimulationStatusEvent> kafkaTemplate;
    private final String topic;

    public SimulationStatusProducer(KafkaTemplate<String, SimulationStatusEvent> kafkaTemplate,
                                     @Value("${kafka.topics.simulation-status}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(Long experimentId, String status) {
        kafkaTemplate.send(topic, experimentId.toString(), new SimulationStatusEvent(experimentId, status));
        log.info("Published: experiment {} is now {}", experimentId, status);
    }
}