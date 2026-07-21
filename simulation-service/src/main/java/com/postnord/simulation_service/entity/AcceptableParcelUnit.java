package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acceptable_parcel_unit")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class AcceptableParcelUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acceptable_parcel_unit_id")
    private Long id;

    private Double minHeight, maxHeight, minLength, maxLength, minWidth, maxWidth, minWeight, maxWeight;

    @Column(name = "experiment_id")
    private Long experimentId; // plain field now — no relationship
}