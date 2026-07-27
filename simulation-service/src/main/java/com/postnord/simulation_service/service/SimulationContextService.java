package com.postnord.simulation_service.service;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.entity.SimulationContext;
import com.postnord.simulation_service.repository.SimulationContextRepository;

@Service
@RequiredArgsConstructor
// It saves important experiment details needed later during simulation.
public class SimulationContextService {

    private final SimulationContextRepository simulationContextRepository;

    public void saveContext(ExperimentDto experimentDto) {
        SimulationContext context = SimulationContext.builder()
                .experimentId(experimentDto.getExperimentId())
                .selectedDate(experimentDto.getSelectedDate())
                .startTime(experimentDto.getStartTime())
                .endTime(experimentDto.getEndTime())
                .maxRecirculationCount(experimentDto.getMaxRecirculationCount())
                .build();
        simulationContextRepository.save(context); // save() overwrites if the ID already exists — fine for a re-run
    }
}