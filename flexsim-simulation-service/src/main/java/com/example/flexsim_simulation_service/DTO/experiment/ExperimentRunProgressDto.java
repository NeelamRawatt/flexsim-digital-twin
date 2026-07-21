package com.example.flexsim_simulation_service.DTO.experiment;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExperimentRunProgressDto {
    private Long runId;
    private Long experimentId;
    private String status;
    private String stage;
    private String message;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}
