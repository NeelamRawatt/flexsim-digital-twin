package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.ParcelDetailsKPIPerTickDTO;
import com.example.flexsim_simulation_service.service.ParcelDetailsKPIPerTickService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/parcelInsights")
@Controller
@RequiredArgsConstructor
public class ParcelDetailsKPIPerTickController
{

    private final ParcelDetailsKPIPerTickService parcelDetailsKPIPerTickService;


    @PostMapping("/saveParcelInsigts")
    private ResponseEntity<List<ParcelDetailsKPIPerTickDTO>> saveParcelInsights
            (@RequestBody List<ParcelDetailsKPIPerTickDTO> dtoList)
    {
        List<ParcelDetailsKPIPerTickDTO> savedList = parcelDetailsKPIPerTickService.saveParcelInsights(dtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }

    @GetMapping("/getByExpId/{simExpId}")
    private ResponseEntity<List<ParcelDetailsKPIPerTickDTO>> getByExpId(@PathVariable Integer simExpId)
    {
        List<ParcelDetailsKPIPerTickDTO> response = parcelDetailsKPIPerTickService.getByExpId(simExpId);
        if(response.isEmpty() )
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);

    }

}
