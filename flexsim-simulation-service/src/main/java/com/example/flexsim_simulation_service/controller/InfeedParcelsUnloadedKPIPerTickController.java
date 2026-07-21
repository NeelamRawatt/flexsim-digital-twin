package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.InfeedParcelsUnloadedKPIPerTickDTO;
import com.example.flexsim_simulation_service.service.InfeedParcelsUnloadedKPIPerTickService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/infeedKPI")
public class InfeedParcelsUnloadedKPIPerTickController {

    private final InfeedParcelsUnloadedKPIPerTickService infeedParcelsUnloadedKPIPerTickService;


    @PostMapping("/saveInsights")
    private ResponseEntity<List<InfeedParcelsUnloadedKPIPerTickDTO>> saveInfeedInsights
            (@RequestBody List<InfeedParcelsUnloadedKPIPerTickDTO> dto)
    {

        List<InfeedParcelsUnloadedKPIPerTickDTO> savedList= infeedParcelsUnloadedKPIPerTickService.saveInfeedInsights(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }


    @GetMapping("/getInsights/{simExpId}")
    private ResponseEntity<List<InfeedParcelsUnloadedKPIPerTickDTO>> getByExpId
            (@PathVariable Integer simExpId)
    {
        List<InfeedParcelsUnloadedKPIPerTickDTO> response = infeedParcelsUnloadedKPIPerTickService.getByExpId(simExpId);
        if(response.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
