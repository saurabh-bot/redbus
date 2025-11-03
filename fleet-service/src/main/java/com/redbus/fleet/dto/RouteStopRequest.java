package com.redbus.fleet.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopRequest {
    
    @NotNull(message = "City code is required")
    private String cityCode;
    
    @NotNull(message = "Sequence is required")
    @Positive(message = "Sequence must be positive")
    private Integer sequence;
    
    @PositiveOrZero(message = "Arrival offset must be zero or positive")
    private Integer arrivalOffsetMinutes;
    
    @PositiveOrZero(message = "Departure offset must be zero or positive")
    private Integer departureOffsetMinutes;
    
    @PositiveOrZero(message = "Fare from start must be zero or positive")
    private Double fareFromStart;
    
    private Boolean isBoardingPoint;
    private Boolean isDroppingPoint;
}
