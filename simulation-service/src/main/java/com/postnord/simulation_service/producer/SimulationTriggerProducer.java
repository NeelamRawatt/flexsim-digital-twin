package com.postnord.simulation_service.producer;



import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.event.SimulationTriggerEvent;

@Component
public class SimulationTriggerProducer {

    private final KafkaTemplate<String, SimulationTriggerEvent> kafkaTemplate;
    private final String topic;

    public SimulationTriggerProducer(KafkaTemplate<String, SimulationTriggerEvent> kafkaTemplate,
                                      @Value("${kafka.topics.simulation-trigger}") String topic)
    {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }


    public void publish(ExperimentDto dto) {
        SimulationTriggerEvent event = new SimulationTriggerEvent(
                dto.getExperimentId(), dto.getSelectedDate(),
                dto.getStartTime(), dto.getEndTime(), dto.getMaxRecirculationCount());
        kafkaTemplate.send(topic, dto.getExperimentId().toString(), event);
    }
}