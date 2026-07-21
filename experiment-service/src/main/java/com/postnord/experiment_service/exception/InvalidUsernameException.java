package com.postnord.experiment_service.exception;

public class InvalidUsernameException extends RuntimeException 
{
    
    public InvalidUsernameException(String username)
    {
        super("No such user: " + username);
    }
}
