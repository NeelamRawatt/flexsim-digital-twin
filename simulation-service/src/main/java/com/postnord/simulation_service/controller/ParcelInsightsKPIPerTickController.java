package com.postnord.simulation_service.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ParcelInsightsKPIPerTickDTO;
import com.postnord.simulation_service.service.ParcelDetailsKPIPerTickService;

import java.util.List;

@RequestMapping("/api/parcelInsights")
@Controller
@RequiredArgsConstructor
public class ParcelInsightsKPIPerTickController
{

    private final ParcelDetailsKPIPerTickService parcelDetailsKPIPerTickService;


    @PostMapping("/saveParcelInsigts")
    private ResponseEntity<List<ParcelInsightsKPIPerTickDTO>> saveParcelInsights
            (@RequestBody List<ParcelInsightsKPIPerTickDTO> dtoList)
    {
        List<ParcelInsightsKPIPerTickDTO> savedList = parcelDetailsKPIPerTickService.saveParcelInsights(dtoList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }

    @GetMapping("/getByExpId/{simExpId}")
    private ResponseEntity<List<ParcelInsightsKPIPerTickDTO>> getByExpId(@PathVariable Integer simExpId)
    {
        List<ParcelInsightsKPIPerTickDTO> response = parcelDetailsKPIPerTickService.getByExpId(simExpId);
        if(response.isEmpty() )
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);

    }

}
