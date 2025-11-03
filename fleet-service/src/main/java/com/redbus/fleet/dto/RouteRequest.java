package com.redbus.fleet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {
    
    @NotNull(message = "Bus ID is required")
    @Positive(message = "Bus ID must be positive")
    private Long busId;
    
    @NotNull(message = "Source city code is required")
    private String sourceCityCode;
    
    @NotNull(message = "Destination city code is required")
    private String destinationCityCode;
    
    @Positive(message = "Distance must be positive")
    private Double distanceKm;
    
    @Positive(message = "Estimated duration must be positive")
    private Integer estimatedDurationMinutes;
    
    private Boolean isActive;
}
