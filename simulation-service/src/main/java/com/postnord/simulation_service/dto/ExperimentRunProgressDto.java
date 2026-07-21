package com.postnord.simulation_service.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

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