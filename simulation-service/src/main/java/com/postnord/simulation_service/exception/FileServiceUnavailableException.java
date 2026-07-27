package com.postnord.simulation_service.exception;

public class FileServiceUnavailableException extends RuntimeException {
    public FileServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}