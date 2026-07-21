
 package com.postnord.experiment_service.exception;

public class AuthServiceUnavailableException extends RuntimeException{

     public AuthServiceUnavailableException(String message, Throwable cause)
     {
        super(message, cause);
     }
}