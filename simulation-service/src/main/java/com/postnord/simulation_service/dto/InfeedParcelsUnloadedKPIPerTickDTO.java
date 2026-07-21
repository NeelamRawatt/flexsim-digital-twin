package com.postnord.simulation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InfeedParcelsUnloadedKPIPerTickDTO {


    private Integer simExpId;

    private Integer tick_value;
    private String infeed_name;
    private Integer parcel_unloaded;
    private Integer shift_id;
}
