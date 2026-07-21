package com.postnord.experiment_service.exception;

public class ExperimentNotFoundException extends RuntimeException {


    public ExperimentNotFoundException(Long id)
    {
        super("Experiment not found with id : " + id);
    }
    
}
