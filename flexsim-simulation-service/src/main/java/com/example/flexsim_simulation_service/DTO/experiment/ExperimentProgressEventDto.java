package com.example.flexsim_simulation_service.DTO.experiment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentProgressEventDto {
    private Long experimentId;
    private String status;   // RUNNING, COMPLETED, FAILED
    private String stage;    // OPENING_FLEXSIM, GETTING_INFEED_DETAILS, etc.
    private String message;  // text shown in loader
}