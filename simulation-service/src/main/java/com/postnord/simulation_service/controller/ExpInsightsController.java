package com.postnord.simulation_service.controller;



import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.postnord.simulation_service.dto.ExpInsightsDTO;
import com.postnord.simulation_service.service.ExpInsightsService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/expinsights")
public class ExpInsightsController {


    private final ExpInsightsService service ;


    @PostMapping("/saveinsights")
    public ResponseEntity<ExpInsightsDTO> createInsights(@RequestBody ExpInsightsDTO dto)
    {
        ExpInsightsDTO savedInsights = service.saveExpInsights(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedInsights);
    }

    @GetMapping("/{simExpId}")
    public ResponseEntity<ExpInsightsDTO> getInsightsByExpId(@PathVariable Integer simExpId)
    {
        ExpInsightsDTO insight=service.getInsightsByExpId(simExpId);
        return ResponseEntity.ok(insight);
    }


}
