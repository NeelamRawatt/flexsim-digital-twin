package com.postnord.simulation_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_context")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class SimulationContext {

    @Id
    private Long experimentId; // same ID as Experiment Service's record — no relationship, just a shared key

    private LocalDate selectedDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxRecirculationCount;
}