package com.postnord.simulation_service.controller;



import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.postnord.simulation_service.dto.ChuteInsightsKPIPerTickDTO;
import com.postnord.simulation_service.service.ChuteInsightsKPIPerTickService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chuteInsights")
public class ChuteInsightsKPIPerTickController {


    private final ChuteInsightsKPIPerTickService chuteInsightsKPIPerTickService;


    @PostMapping("/saveChuteInsights")
    public ResponseEntity<List<ChuteInsightsKPIPerTickDTO>>  saveInsights(@RequestBody List<ChuteInsightsKPIPerTickDTO> dto)
    {
        List<ChuteInsightsKPIPerTickDTO> savedDTO= chuteInsightsKPIPerTickService.saveInsights(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedDTO);
    }

    @GetMapping("/getChuteInsights/{simExpId}")
    public ResponseEntity<List<ChuteInsightsKPIPerTickDTO>> getBySimExpId(@PathVariable Integer simExpId) {
        return ResponseEntity.ok(chuteInsightsKPIPerTickService.getBySimExpId(simExpId));
    }



}
