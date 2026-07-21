package com.postnord.simulation_service.dto;

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