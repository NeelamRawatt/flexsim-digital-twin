package com.example.flexsim_simulation_service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flexsim")
@Getter
@Setter
public class FlexsimProperties {
    private String path;
}
