package com.postnord.simulation_service.exception;


public class SimulationContextNotFoundException extends RuntimeException {
    public SimulationContextNotFoundException(Long experimentId) {
        super("No simulation context found for experiment: " + experimentId
                + ". Was startSimulation() called first?");
    }
}