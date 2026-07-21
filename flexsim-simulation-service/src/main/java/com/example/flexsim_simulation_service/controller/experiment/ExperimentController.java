package com.example.flexsim_simulation_service.controller.experiment;

import com.example.flexsim_simulation_service.DTO.experiment.ExperimentDto;
import com.example.flexsim_simulation_service.service.experiment.ExperimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiments")
@RequiredArgsConstructor

public class ExperimentController {

    private final ExperimentService experimentService;

    @GetMapping("/history")
    public Page<ExperimentDto> getHistory(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParts = sort.split(",");
        String sortField = sortParts.length > 0 ? sortParts[0] : "createdAt";
        Sort.Direction direction = (sortParts.length > 1 && "asc".equalsIgnoreCase(sortParts[1]))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return experimentService.getHistory(username, pageable);
    }


//    @GetMapping("/getExperiments/{username}")
//    public ResponseEntity<List<ExperimentDto>> getExperimentsForUser(@PathVariable String username){
//        List<ExperimentDto> experiments = experimentService.getExperimentsForUser(username);
//        return ResponseEntity.ok(experiments);
//    }

    @PostMapping("/createExperiment")
    public ResponseEntity<ExperimentDto> createExperiment(@RequestBody ExperimentDto dto) {

        ExperimentDto savedDto = experimentService.saveExperiment(dto);

        return ResponseEntity.ok(savedDto);

    }

    @GetMapping("/{experimentId}")
    public ResponseEntity<ExperimentDto> getExperimentById(@PathVariable Long experimentId) {

        ExperimentDto dto = experimentService.getExperimentById(experimentId);

        return ResponseEntity.ok(dto);

    }

    @PutMapping("/{experimentId}")
    public ResponseEntity<ExperimentDto> updateExperiment(

            @PathVariable Long experimentId,

            @RequestBody ExperimentDto dto

    ) {

        ExperimentDto updatedDto = experimentService.updateExperiment(experimentId, dto);

        return ResponseEntity.ok(updatedDto);

    }

    @DeleteMapping("/{experimentId}")
    public ResponseEntity<String> deleteExperiment(@PathVariable Long experimentId) {

        experimentService.deleteExperiment(experimentId);

        return ResponseEntity.ok("Experiment  deleted successfully");

    }

}
