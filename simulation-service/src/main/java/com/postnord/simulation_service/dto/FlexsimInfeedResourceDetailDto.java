package com.postnord.simulation_service.dto;



import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlexsimInfeedResourceDetailDto {
    Integer shiftId;
    String tc;
    Integer noOfResources;
}
