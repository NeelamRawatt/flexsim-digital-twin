package com.postnord.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class ErrorResponseDTO {
    private String field;
    private String message;
}
