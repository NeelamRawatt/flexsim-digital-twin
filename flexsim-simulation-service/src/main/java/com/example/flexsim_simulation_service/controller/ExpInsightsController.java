package com.example.flexsim_simulation_service.controller;


import com.example.flexsim_simulation_service.DTO.ExpInsightsDTO;
import com.example.flexsim_simulation_service.service.ExpInsightsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
