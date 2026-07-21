package com.postnord.simulation_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "infeed_parcels_unloaded_kpi_per_tick")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InfeedParcelsUnloadedKPIPerTick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq_id;

    @Column(name="sim_exp_id")
    private Integer simExpId;

    private Integer tick_value;
    private String infeed_name;
    private Integer parcel_unloaded;
    private Integer shift_id;



}
