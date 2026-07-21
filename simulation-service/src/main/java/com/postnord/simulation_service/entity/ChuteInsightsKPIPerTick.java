package com.postnord.simulation_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chute_insights_kpi_per_tick")
public class ChuteInsightsKPIPerTick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq_id;

    @Column(name="sim_exp_id")
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
