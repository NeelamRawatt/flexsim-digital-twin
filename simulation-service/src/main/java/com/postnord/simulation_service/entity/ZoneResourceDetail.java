package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "zone_resource_detail")
@Getter @Setter
public class ZoneResourceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer shiftId;
    private String zoneId;
    private Integer resourceId;
    private String chutes;

    @Column(name = "experiment_id")
    private Long experimentId; // plain field — no relationship to Experiment Service's table
}