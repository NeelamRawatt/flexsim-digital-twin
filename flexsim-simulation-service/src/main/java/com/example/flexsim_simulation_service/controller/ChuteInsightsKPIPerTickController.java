package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.ChuteInsightsKPIPerTickDTO;
import com.example.flexsim_simulation_service.service.ChuteInsightsKPIPerTickService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}
