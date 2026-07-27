package com.postnord.simulation_service.exception;

public class FilesNotReadyException extends RuntimeException {
    public FilesNotReadyException(Long experimentId) {
        super("Required files not yet uploaded for experiment: " + experimentId);
    }
}