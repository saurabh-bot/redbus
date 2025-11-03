package com.redbus.fleet.dto;

import com.redbus.fleet.model.enums.BusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusResponse {
    
    private Long busId;
    private Long operatorId;
    private String busNumber;
    private BusType seatType;
    private Boolean isActive;
    private Boolean isAc;
    private Map<String, Object> amenities;
    private Integer totalSeats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static LocalDateTime epochToDateTime(Long epoch) {
        return epoch != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC) : null;
    }
}
