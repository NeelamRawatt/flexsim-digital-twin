package com.postnord.simulation_service.entity;





import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chute_details_kpi")
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ChuteDetailsKPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq_id;

    @Column(name = "sim_exp_id")
    private Integer simExpId;
    private Integer chute_id;
    private String type;
    private Integer containers_filled;
    private Integer parcel_filled_in_cage;
    private Integer parcel_throughput;
    private Integer parcel_blocked;
    private Integer parcel_handled_resource;
}
