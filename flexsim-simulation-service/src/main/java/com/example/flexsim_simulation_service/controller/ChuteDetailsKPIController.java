package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.ChuteDetailsKPIDTO;
import com.example.flexsim_simulation_service.service.ChuteDetailsKPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chutedetails")
public class ChuteDetailsKPIController {

    private final ChuteDetailsKPIService chuteDetailsKPIService;


    @PostMapping("/saveChutedata")
    private ResponseEntity<List<ChuteDetailsKPIDTO>> saveChuteDetails
            (@RequestBody List<ChuteDetailsKPIDTO> dtoList )
    {
        List<ChuteDetailsKPIDTO> savedList= chuteDetailsKPIService.saveChuteDetails(dtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }

    @GetMapping("/getChuteKPI/{simExpId}")
    private ResponseEntity<List<ChuteDetailsKPIDTO>> getBySimExpId(@PathVariable Integer simExpId)
    {
        List<ChuteDetailsKPIDTO> response = chuteDetailsKPIService.getBySimExpId(simExpId);
        if(response.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }



}
