package com.example.flexsim_simulation_service.DTO.resourceDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResourceDetailDto {

    private Long experimentId;
    private String shiftId ;
    private String zoneId ;
    private Integer resourceId;
    private String chutes;
}