package com.postnord.auth_service.dto;

import lombok.*;

@Getter @Builder @AllArgsConstructor
public class LoginResponseDTO {


    private String token;
    private String username;
    private String message;
    
}
