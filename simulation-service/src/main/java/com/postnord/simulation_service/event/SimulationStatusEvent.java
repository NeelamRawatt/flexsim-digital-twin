package com.postnord.simulation_service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor @NoArgsConstructor
public class SimulationStatusEvent {
    private Long experimentId;
    private String status;
}
