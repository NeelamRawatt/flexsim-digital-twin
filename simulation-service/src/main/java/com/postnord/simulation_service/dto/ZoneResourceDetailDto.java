package com.postnord.simulation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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