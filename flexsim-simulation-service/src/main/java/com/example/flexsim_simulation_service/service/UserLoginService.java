package com.example.flexsim_simulation_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.flexsim_simulation_service.entity.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.flexsim_simulation_service.DTO.LoginErrorResponseDTO;
import com.example.flexsim_simulation_service.DTO.UserLoginRequestDTO;
import com.example.flexsim_simulation_service.DTO.UserLoginResponseDTO;
import com.example.flexsim_simulation_service.repository.UserLoginRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;

    public ResponseEntity<?> login(UserLoginRequestDTO requestDTO) {

        List<String> errors = new ArrayList<>();

        if (requestDTO.getUsername() == null || requestDTO.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(
                LoginErrorResponseDTO.builder()
            .field("username")
        .message("USERNAME IS REQUIRED")
        .build()
            );
           
        }

        if (requestDTO.getPassword() == null) {
            return ResponseEntity.badRequest().body(
                LoginErrorResponseDTO.builder()
            .field("password")
        .message("PASSWORD IS REQUIRED")
        .build()
            );
        }

      
        User user = userLoginRepository.findByUsername(requestDTO.getUsername());

        if (user==null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginErrorResponseDTO.builder()
            .field("username")
        .message("Invalid Username")
        .build());
        }

       

        if (!user.getPassword().equals(requestDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(LoginErrorResponseDTO.builder()
            .field("password")
        .message("Invalid Password")
        .build());
        }

        UserLoginResponseDTO response = UserLoginResponseDTO.builder()
                .message("Login successful")
                .username(user.getUsername())
                .build();

        return ResponseEntity.ok(response);

    }

}
