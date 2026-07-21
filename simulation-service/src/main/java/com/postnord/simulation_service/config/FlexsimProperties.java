package com.postnord.simulation_service.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flexsim")
@Getter @Setter
public class FlexsimProperties {
    private String mode;          // "dummy" or "real"
    private String path;          // used only in real mode
    private String callbackHost;  // used only in real mode
    private int callbackPort;     // used only in real mode
    private long dummyStageDelayMs;
}