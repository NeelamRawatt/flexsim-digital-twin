package com.postnord.simulation_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChuteInsightsKPIPerTickDTO {

    private Integer simExpId;
    private Integer tickValue;
    private Integer chuteId;
    private Integer chuteType;
    private Integer parcel_scanned;
    private Integer parcel_throughput;
    private Integer parcel_blocked;
    private Integer shift_id;
    private Integer parcel_weight;
}
