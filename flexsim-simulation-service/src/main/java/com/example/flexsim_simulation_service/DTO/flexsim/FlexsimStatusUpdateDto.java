package com.example.flexsim_simulation_service.DTO.flexsim;

import com.example.flexsim_simulation_service.enums.ExperimentRunStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlexsimStatusUpdateDto {
    private Long experimentId;
    private ExperimentRunStage experimentRunStage;
    private String message;
}
