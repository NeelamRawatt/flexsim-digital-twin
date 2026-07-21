package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parcel_insights_kpi_per_tick")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelInsightsKPIPerTick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq_id;

    @Column(name="sim_exp_id")
    private Integer simExpId;
    private Integer tick_value;
    private Integer parcel_scanned;
    private Integer parcel_throughput;
    private Integer parcel_rejected;
    private Integer parcel_throughput_without_rejected;
    private Integer parcel_blocked;
    private Integer shift_id;

}
