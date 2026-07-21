package com.postnord.simulation_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ResourceWeightHandledKPIDTO;
import com.postnord.simulation_service.service.ResourceWeightHandledKPIService;

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
