package com.postnord.simulation_service.exception;


public class InsightsNotFoundException extends RuntimeException {
    public InsightsNotFoundException(Integer simExpId) {
        super("Insights not found for experiment id: " + simExpId);
    }
}