package com.postnord.simulation_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SimulationRunNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(SimulationRunNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(SimulationContextNotFoundException.class)
public ResponseEntity<Map<String, String>> handleContextNotFound(SimulationContextNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(InsightsNotFoundException.class)
public ResponseEntity<Map<String, String>> handleInsightsNotFound(InsightsNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(AcceptableParcelUnitNotFoundException.class)
public ResponseEntity<Map<String, String>> handleAcceptableParcelUnitNotFound(InsightsNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(FilesNotReadyException.class)
public ResponseEntity<Map<String, String>> handleFilesNotReady(FilesNotReadyException ex) {
    return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
}

@ExceptionHandler(FileServiceUnavailableException.class)
public ResponseEntity<Map<String, String>> handleFileServiceUnavailable(FileServiceUnavailableException ex) {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(Map.of("error", "Could not verify uploaded files right now. Please try again shortly."));
}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Something went wrong: " + ex.getMessage()));
    }
}