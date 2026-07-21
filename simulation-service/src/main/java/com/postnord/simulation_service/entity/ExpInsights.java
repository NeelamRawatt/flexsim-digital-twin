package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "exp_insights")
public class ExpInsights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long insight_id;

    @Column(name = "sim_exp_id")
    private Integer simExpId;
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