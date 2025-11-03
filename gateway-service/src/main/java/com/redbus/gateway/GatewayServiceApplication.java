package com.redbus.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration;
import org.springframework.cloud.gateway.config.GatewayMetricsAutoConfiguration;

@SpringBootApplication(exclude = {
    SecurityAutoConfiguration.class,
    GatewayAutoConfiguration.class,
    GatewayClassPathWarningAutoConfiguration.class,
    GatewayMetricsAutoConfiguration.class
})
public class GatewayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }
}
