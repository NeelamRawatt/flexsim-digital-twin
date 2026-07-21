package com.postnord.experiment_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class ExperimentDto {
    
    private Long experimentId;

    @NotBlank(message = "Experiment name is required")
    private String experimentName;

    private String terminal;
    private String sortingType;
    private int useCaseId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate selectedDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer parcelCount;
    private Integer newParcelCount;
    private Integer parcelChangeValue;
    private String parcelChangeMode;
    private Integer maxRecirculationCount;

    @NotBlank(message = "Username is required")
    private String username;

    private String status;
    private LocalDateTime createdAt;
}
