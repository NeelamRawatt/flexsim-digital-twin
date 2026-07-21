package com.postnord.simulation_service.controller;



import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.InfeedParcelsUnloadedKPIPerTickDTO;
import com.postnord.simulation_service.service.InfeedParcelsUnloadedKPIPerTickService;

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
