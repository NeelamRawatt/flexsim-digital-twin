package com.example.flexsim_simulation_service.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChuteDetailsKPIDTO {

    private Integer sim_exp_id;
    private Integer chute_id;
    private String type;
    private Integer containers_filled;
    private Integer parcel_filled_in_cage;
    private Integer parcel_throughput;
    private Integer parcel_blocked;
    private Integer parcel_handled_resource;

}
