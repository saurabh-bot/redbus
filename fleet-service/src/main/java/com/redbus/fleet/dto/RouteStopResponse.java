package com.redbus.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopResponse {
    
    private Long routeStopId;
    private Long routeId;
    private Long cityId;
    private String cityCode;
    private String cityName;
    private Integer sequence;
    private Boolean isBoardingPoint;
    private Boolean isDroppingPoint;
    private Integer arrivalOffsetMinutes;
    private Integer departureOffsetMinutes;
    private Double fareFromStart;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static LocalDateTime epochToDateTime(Long epoch) {
        return epoch != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC) : null;
    }
}
