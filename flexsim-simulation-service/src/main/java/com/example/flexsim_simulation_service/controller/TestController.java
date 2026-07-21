package com.example.flexsim_simulation_service.controller;

import com.example.flexsim_simulation_service.DTO.UserLoginResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<List<UserLoginResponseDTO>> test(){

        List<UserLoginResponseDTO> sampleUsers = List.of(
            new UserLoginResponseDTO("User1 logged in successfully", "User1"),
            new UserLoginResponseDTO("User2 logged in successfully", "User2"),
            new UserLoginResponseDTO("User3 logged in successfully", "User3")
        );
        return ResponseEntity.ok(sampleUsers);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> test(){
//        System.out.println("sdlgjnsdljdnsf");
//        return ResponseEntity.ok("sdgsgsfdg");
//    }
}
