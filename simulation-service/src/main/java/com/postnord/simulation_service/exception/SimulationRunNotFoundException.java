package com.postnord.simulation_service.exception;

public class SimulationRunNotFoundException extends RuntimeException {
    public SimulationRunNotFoundException(Long experimentId) {
        super("No simulation run found for experiment: " + experimentId);
    }
}
