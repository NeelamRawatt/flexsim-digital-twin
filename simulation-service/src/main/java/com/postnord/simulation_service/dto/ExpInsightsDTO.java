package com.postnord.simulation_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpInsightsDTO {

    private Integer sim_exp_id;
    private Integer total_parcels_scanned;
    private Integer total_parcels_blocked;
    private Integer total_parcel_throughput;
    private Integer total_parcels_unloaded;
    private Integer total_parcels_rejected;
    private Integer max_recirculation_count;
    private Integer total_parcels_throughput_without_rejected;
    private Integer total_recirculation_count;
    private Integer total_parcel_not_sorted;


}
