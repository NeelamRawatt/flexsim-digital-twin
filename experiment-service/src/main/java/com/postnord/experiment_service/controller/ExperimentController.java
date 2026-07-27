package com.postnord.experiment_service.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postnord.experiment_service.dto.ExperimentDto;
import com.postnord.experiment_service.service.ExperimentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
// This is the base URL for this controller.
@RequestMapping("/api/experiments")
@RequiredArgsConstructor
public class ExperimentController {
    
    private final ExperimentService experimentService;


    // @PostMapping
    // public ResponseEntity<ExperimentDto> createExperiment(@Valid @RequestBody ExperimentDto dto )
    // {
    //     return ResponseEntity.ok(experimentService.createExperiment(dto));
    // }

    // controller/ExperimentController.java
@PostMapping
public ResponseEntity<ExperimentDto> createExperiment(@Valid @RequestBody ExperimentDto dto, Authentication authentication) {
    dto.setUsername(authentication.getName()); // overrides whatever was in the body — identity comes only from the verified token
    return ResponseEntity.ok(experimentService.createExperiment(dto));
}

@GetMapping("/history")
public Page<ExperimentDto> getHistory(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "8") int size,
        @RequestParam(defaultValue = "createdAt,desc") String sort
) {
    String username = authentication.getName();
    String[] sortParts = sort.split(",");
    Sort.Direction direction = (sortParts.length > 1 && "asc".equalsIgnoreCase(sortParts[1]))
            ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
    return experimentService.getHistory(username, pageable);
}

    @GetMapping("/{experimentId}")
    public ResponseEntity<ExperimentDto> getExperimentById(@PathVariable Long experimentId)
    {
        return ResponseEntity.ok(experimentService.getExperimentById(experimentId));
    }

    @DeleteMapping("/{experimentId}")
    public ResponseEntity<String> deleteExperiment(@PathVariable Long experimentId)
    {
        experimentService.deleteExperiment(experimentId);
        return ResponseEntity.ok("Experiment deleted succesfully");
    }

    @PutMapping("/{experimentId}")
    public ResponseEntity<ExperimentDto> updateExperiment(@PathVariable Long experimentId,@Valid @RequestBody ExperimentDto dto)
    {
        return ResponseEntity.ok(experimentService.updateExperiment(experimentId, dto));
    }


    // @GetMapping("/history")
    // public Page<ExperimentDto> getHistory(
    //         @RequestParam String username,
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "8") int size,
    //         @RequestParam(defaultValue = "createdAt,desc") String sort
    // ) {
    //     String[] sortParts = sort.split(",");
    //     Sort.Direction direction = (sortParts.length > 1 && "asc".equalsIgnoreCase(sortParts[1]))
    //             ? Sort.Direction.ASC : Sort.Direction.DESC;
    //     Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParts[0]));
    //     return experimentService.getHistory(username, pageable);
    // }
}
