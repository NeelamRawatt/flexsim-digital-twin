package com.postnord.simulation_service.service;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.producer.SimulationStatusProducer;
import com.postnord.simulation_service.producer.SimulationTriggerProducer;

@Service
@RequiredArgsConstructor
public class SimulationRunService {

    private final SimulationContextService simulationContextService;
    private final SimulationStatusProducer simulationStatusProducer;
    private final SimulationTriggerProducer simulationTriggerProducer;

    public ResponseEntity<String> submitSimulation(ExperimentDto experimentDto) {
        simulationContextService.saveContext(experimentDto); // still needed immediately, for whenever this one's turn comes
        simulationStatusProducer.publish(experimentDto.getExperimentId(), "QUEUED");
        simulationTriggerProducer.publish(experimentDto);
        return ResponseEntity.ok("Simulation queued successfully");
    }
}