package com.redbus.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "search.service")
@Data
public class SearchServiceConfig {
    private String url = "http://localhost:8082";
    
    public String getUrl() {
        return url != null ? url : "http://localhost:8082";
    }
}
