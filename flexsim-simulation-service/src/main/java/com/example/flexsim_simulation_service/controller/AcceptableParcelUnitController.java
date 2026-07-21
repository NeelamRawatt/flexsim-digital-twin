package com.example.flexsim_simulation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.flexsim_simulation_service.DTO.AcceptableParcelUnitDTO;
import com.example.flexsim_simulation_service.service.AcceptableParcelUnitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/acceptable-parcel-unit")
@RequiredArgsConstructor
public class AcceptableParcelUnitController {
    private final AcceptableParcelUnitService acceptableParcelUnitService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody AcceptableParcelUnitDTO dto) {
      System.out.println("DTO :" + dto);
      acceptableParcelUnitService.save(dto);
        return ResponseEntity.ok("Acceptable Parcel Unit Saved succesfully");
    }
}