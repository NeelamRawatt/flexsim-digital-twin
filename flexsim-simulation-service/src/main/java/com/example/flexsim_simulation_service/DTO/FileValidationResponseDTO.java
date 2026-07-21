package com.example.flexsim_simulation_service.DTO;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileValidationResponseDTO {

    private boolean valid;
    private String message;
    private List<String> errors;
}
