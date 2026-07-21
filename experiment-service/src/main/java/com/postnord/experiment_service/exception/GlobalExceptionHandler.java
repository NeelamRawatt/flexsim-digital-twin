package com.postnord.experiment_service.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ExperimentNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(ExperimentNotFoundException ex)
    {
        Map<String, String> body = new HashMap<>();
        body.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    
    }

     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        Map<String, String> body = new HashMap<>();
        body.put("field", fieldError != null ? fieldError.getField() : "unknown");
        body.put("message", fieldError != null ? fieldError.getDefaultMessage() : "Validation failed");
        return ResponseEntity.badRequest().body(body);
    }


    @ExceptionHandler(InvalidUsernameException.class)
    public ResponseEntity<Map<String,String>> handleInvalidUsername(InvalidUsernameException ex)
    {
        return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()));
    }


    @ExceptionHandler(AuthServiceUnavailableException.class)
    public ResponseEntity<Map<String,String>> handleAuthUnavailable(AuthServiceUnavailableException ex)
    {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                            .body(Map.of("error","Could not verify user right now , Please try again shortly"));
    }

     @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpected(Exception ex) {
        Map<String, String> body = new HashMap<>();
        body.put("error", "Something went wrong. Please try again.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
    
}
