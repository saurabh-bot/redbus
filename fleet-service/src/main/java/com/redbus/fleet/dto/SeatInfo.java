package com.redbus.fleet.dto;

import com.redbus.fleet.model.enums.Deck;
import com.redbus.fleet.model.enums.SeatAvailabilityStatus;
import com.redbus.fleet.model.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatInfo {
    private String seatNumber;
    private SeatType seatType;
    private Deck deck;
    private SeatAvailabilityStatus availabilityStatus;
    private Double price;
    private Boolean isLadiesSeat;
    private Integer positionX;
    private Integer positionY;
    private Integer rowNumber;
    private Integer columnNumber;
}
