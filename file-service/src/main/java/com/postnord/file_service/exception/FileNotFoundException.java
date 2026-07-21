package com.postnord.file_service.exception;

public class FileNotFoundException extends RuntimeException{

    public FileNotFoundException(Long id)
    {
        super("Uploaded File not found with id: " + id);
    }
    
}
