package com.example.flexsim_simulation_service.DTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationResultDTO {

    private double throughput;
    private double blockage;
    private double utilization;
}
