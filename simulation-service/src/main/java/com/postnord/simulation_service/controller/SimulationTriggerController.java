package com.postnord.simulation_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postnord.simulation_service.dto.ExperimentDto;
import com.postnord.simulation_service.service.SimulationRunService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationTriggerController {

    private final SimulationRunService simulationRunService;

    @PostMapping("/startSimulation")
    public ResponseEntity<String> startSimulation(@RequestBody ExperimentDto experimentDto)
    {

        return simulationRunService.submitSimulation(experimentDto);
    }


//    @PostMapping("/saveResults")
//    public ResponseEntity<SimulationResultDTO> saveResult(@RequestBody SimulationResultDTO simulationResultDTO)
//    {
//        return simulationResultService.saveResult(simulationResultDTO);
//    }



}
