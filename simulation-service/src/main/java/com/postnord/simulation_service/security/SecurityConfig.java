package com.postnord.simulation_service.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // FlexSim's own live callbacks -- FlexSim is a program, not a logged-in user,
                // so it can never have a token. These stay open on purpose.
                .requestMatchers(
                    "/api/parcels/**",
                    "/api/resourceDetails/**",
                    "/api/flexsim/updateSimulationStage",
                    "/api/chutedetails/**",
                    "/api/expinsights/**",
                    "/api/resourcekpi/**",
                    "/api/infeedKPI/**",
                    "/api/parcelInsights/**",
                    "/api/chuteInsights/**",
                    "/api/acceptable-parcel-unit/**"
                ).permitAll()
                // Everything else -- a real person, through the frontend, needs a token
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}