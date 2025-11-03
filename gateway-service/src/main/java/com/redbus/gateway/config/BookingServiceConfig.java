package com.redbus.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "booking.service")
@Data
public class BookingServiceConfig {
    private String url = "http://localhost:8083";
    
    public String getUrl() {
        return url != null ? url : "http://localhost:8083";
    }
}

