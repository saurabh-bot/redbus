package com.redbus.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusSearchResult {
    private Long tripId;
    private String busNumber;
    private LocalDateTime boardingTime;
    private LocalDateTime droppingTime;
    private Double fare;
    private List<String> amenities;
    private Integer availableSeats;
    private String seatType; // SEATER, SLEEPER
    private String sourceCityCode;
    private String destinationCityCode;
    private Integer fromStopSequence;
    private Integer toStopSequence;
    
    public static LocalDateTime epochToDateTime(Long epoch) {
        return epoch != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneOffset.UTC) : null;
    }
}
