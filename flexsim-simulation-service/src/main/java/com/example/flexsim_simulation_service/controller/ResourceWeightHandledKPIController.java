package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.ResourceWeightHandledKPIDTO;
import com.example.flexsim_simulation_service.service.ResourceWeightHandledKPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resourcekpi")
public class ResourceWeightHandledKPIController
{

    private final ResourceWeightHandledKPIService resourceWeightHandledKPIService;


    @PostMapping("/saveResourceKPI")
    public ResponseEntity<List<ResourceWeightHandledKPIDTO>> saveResourceKPI(@RequestBody List<ResourceWeightHandledKPIDTO> dto)
    {

        List<ResourceWeightHandledKPIDTO> savedList= resourceWeightHandledKPIService.saveResourceKPI(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);

    }

    @GetMapping("/getBySimExpId/{simExpId}")
    public ResponseEntity<List<ResourceWeightHandledKPIDTO>> getResourceKPIByExpId(@PathVariable Integer simExpId)
    {

        List<ResourceWeightHandledKPIDTO> response = resourceWeightHandledKPIService.getResourceByExpId(simExpId);
        if(response.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);

    }

}
