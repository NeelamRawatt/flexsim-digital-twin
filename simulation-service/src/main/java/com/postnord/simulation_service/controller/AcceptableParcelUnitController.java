package com.postnord.simulation_service.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.AcceptableParcelUnitDTO;
import com.postnord.simulation_service.service.AcceptableParcelUnitService;

@RestController
@RequestMapping("/api/acceptable-parcel-unit")
@RequiredArgsConstructor
public class AcceptableParcelUnitController {
    private final AcceptableParcelUnitService service;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AcceptableParcelUnitDTO dto) {
        service.save(dto);
        return ResponseEntity.ok("Acceptable Parcel Unit Saved successfully");
    }

    @GetMapping("/{experimentId}")
    public ResponseEntity<AcceptableParcelUnitDTO> getByExperimentId(@PathVariable Long experimentId) {
        return ResponseEntity.ok(service.getByExperimentId(experimentId));
    }
}