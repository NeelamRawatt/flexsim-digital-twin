package com.postnord.simulation_service.event;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class SimulationTriggerEvent {

    private Long experimentId;
    private LocalDate selecteDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer maxRecirculationCount;
    
}
