package com.example.flexsim_simulation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.flexsim_simulation_service.DTO.UserLoginRequestDTO;
import com.example.flexsim_simulation_service.service.UserLoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserLoginController {

    private final UserLoginService userLoginService;


    @PostMapping("/login" )
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO requestDTO)
    {
        return userLoginService.login(requestDTO);
    }
    
}
