package com.example.flexsim_simulation_service.controller.flexsim;


import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.service.SimulationRunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/simulation")
public class SimulationTriggerController {

    private final SimulationRunService simulationRunService;

    @PostMapping("/startSimulation")
    public ResponseEntity<String> startSimulation(@RequestBody ExperimentDto experimentDto)
    {

        return simulationRunService.startSimulation(experimentDto);
    }


//    @PostMapping("/saveResults")
//    public ResponseEntity<SimulationResultDTO> saveResult(@RequestBody SimulationResultDTO simulationResultDTO)
//    {
//        return simulationResultService.saveResult(simulationResultDTO);
//    }



}
