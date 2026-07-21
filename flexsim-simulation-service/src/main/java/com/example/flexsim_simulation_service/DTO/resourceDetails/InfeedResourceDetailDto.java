package com.example.flexsim_simulation_service.DTO.resourceDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfeedResourceDetailDto {

    private Long experimentId;
    private Integer shiftId;
    private String zoneId;
    private String infeed;
    private String tc;
    private Integer noOfResources;
    private Boolean active;

}
