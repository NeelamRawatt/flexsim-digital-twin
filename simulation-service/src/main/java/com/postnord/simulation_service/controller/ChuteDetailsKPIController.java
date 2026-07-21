package com.postnord.simulation_service.controller;




import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ChuteDetailsKPIDTO;
import com.postnord.simulation_service.service.ChuteDetailsKPIService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chutedetails")
public class ChuteDetailsKPIController {
    private final ChuteDetailsKPIService service;

    @PostMapping("/saveChutedata")
    public ResponseEntity<List<ChuteDetailsKPIDTO>> saveChuteDetails(@RequestBody List<ChuteDetailsKPIDTO> dtoList) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveChuteDetails(dtoList));
    }

    @GetMapping("/getChuteKPI/{simExpId}")
    public ResponseEntity<List<ChuteDetailsKPIDTO>> getBySimExpId(@PathVariable Integer simExpId) {
        List<ChuteDetailsKPIDTO> response = service.getBySimExpId(simExpId);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
