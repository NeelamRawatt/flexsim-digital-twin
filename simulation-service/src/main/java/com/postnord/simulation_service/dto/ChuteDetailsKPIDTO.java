package com.postnord.simulation_service.dto;


import lombok.*;

@Builder @NoArgsConstructor @AllArgsConstructor @Data
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
