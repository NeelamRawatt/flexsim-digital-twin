package com.postnord.auth_service.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.postnord.auth_service.dto.LoginRequestDTO;
import com.postnord.auth_service.dto.LoginResponseDTO;
import com.postnord.auth_service.dto.RegisterRequestDTO;
import com.postnord.auth_service.entity.User;
import com.postnord.auth_service.exception.InvalidCredentialsException;
import com.postnord.auth_service.exception.UserAlreadyExistsException;
import com.postnord.auth_service.repository.UserRepository;
import com.postnord.auth_service.security.JwtUtil;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO register(RegisterRequestDTO registerRequestDTO)
    {
        if(userRepository.existsByUsername(registerRequestDTO.getUsername()))
        {
            throw new UserAlreadyExistsException("Username already taken ");
        }

        User user = User.builder()
                    .username(registerRequestDTO.getUsername())
                    .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                    .build();

            userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return LoginResponseDTO.builder()
                .token(token)
                .username(user.getUsername())
                .message("Registered Succesfuly")
                .build();
    }


    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = findByUsername(request.getUsername());

        // Same error for "no such user" and "wrong password" on purpose —
        // telling them apart lets an attacker figure out which usernames exist
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return LoginResponseDTO.builder()
                .token(token).username(user.getUsername()).message("Login successful")
                .build();
    }


    // @Cacheable(value = "users", key = "#username")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public boolean userExists(String username)
    {
        return userRepository.existsByUsername(username);
    }

   
}
