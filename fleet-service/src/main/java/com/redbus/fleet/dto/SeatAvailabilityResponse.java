package com.redbus.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailabilityResponse {
    private Long tripId;
    private Integer fromStopSequence;
    private Integer toStopSequence;
    private Integer availableSeats;
    private Integer totalSeats;
    private List<SeatInfo> seatMap;
}
