package com.postnord.simulation_service.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlexsimZoneResourceDetailDto {
    Integer shiftId;
    String zoneId;
    Integer resourceId;
    String chutes;
    List<Integer> chuteIds;
}
