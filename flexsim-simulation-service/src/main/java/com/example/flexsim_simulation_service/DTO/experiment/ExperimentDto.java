package com.example.flexsim_simulation_service.DTO.experiment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentDto {


    private Long experimentId;
    private String experimentName;
    private String terminal;
    private String sortingType;
    private int useCaseId;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate selectedDate;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "23:00:00")
    private LocalDateTime startTime;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "06:30:00")
    private LocalDateTime endTime;

    private Integer parcelCount;
    private Integer newParcelCount;
    private Integer parcelChangeValue;
    private String parcelChangeMode; // increase / decrease
    private Integer maxRecirculationCount;

    private String username;
    private LocalDateTime createdAt;

    private String status;

}
