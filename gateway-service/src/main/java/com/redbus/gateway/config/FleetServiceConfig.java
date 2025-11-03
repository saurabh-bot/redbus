package com.redbus.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fleet")
@Data
public class FleetServiceConfig {
    private String serviceUrl = "http://localhost:8081";
    
    public String getServiceUrl() {
        return serviceUrl != null ? serviceUrl : "http://localhost:8081";
    }
}
