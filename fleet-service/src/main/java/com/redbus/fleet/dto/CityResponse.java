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
public class CityResponse {
    
    private String cityCode;
    private String cityName;
    private String state;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static LocalDateTime epochToDateTime(Long epoch) {
        return epoch != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC) : null;
    }
}
