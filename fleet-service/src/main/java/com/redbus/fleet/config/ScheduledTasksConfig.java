package com.redbus.fleet.config;

import com.redbus.fleet.service.TripInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "app.trip-generation.enabled", havingValue = "true", matchIfMissing = true)
public class ScheduledTasksConfig {
    
    private final TripInstanceService tripInstanceService;
    
    /**
     * Runs daily at 1:00 AM to generate trip instances for the 180th day
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void generateTripInstances() {
        log.info("Scheduled job triggered: Generating trip instances for next 180 days");
        try {
            tripInstanceService.generateTripInstancesForNext180Days();
            log.info("Scheduled job completed successfully");
        } catch (Exception e) {
            log.error("Error in scheduled trip instance generation", e);
        }
    }
}
