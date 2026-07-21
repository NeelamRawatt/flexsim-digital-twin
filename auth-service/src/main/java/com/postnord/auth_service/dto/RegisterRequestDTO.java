package com.postnord.auth_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequestDTO {


    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message="Password is required")
    @Size(min=6, message="Password must be at least 6 characters ")
    private String password;

    
}
