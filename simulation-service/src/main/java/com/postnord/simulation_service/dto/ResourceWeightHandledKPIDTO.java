package com.postnord.simulation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceWeightHandledKPIDTO {

    private Integer sim_exp_id;
    private Integer resource_id;
    private String zone_name;
    private String chute_id;
    private Integer total_parcels_handled;
    private Integer total_weight_handled;
    private Integer tick_value;
    private Integer shift_id;
}
