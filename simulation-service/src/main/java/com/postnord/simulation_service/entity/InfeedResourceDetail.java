package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "infeed_resource_detail")
@Getter @Setter
public class InfeedResourceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer shiftId;
    private String tc;
    private String infeed;
    private String zoneId;
    private Integer noOfResources;
    private Boolean active;

    @Column(name = "experiment_id")
    private Long experimentId;
}
