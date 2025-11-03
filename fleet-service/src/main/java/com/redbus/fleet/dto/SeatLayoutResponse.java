package com.redbus.fleet.dto;

import com.redbus.fleet.model.enums.Deck;
import com.redbus.fleet.model.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayoutResponse {
    
    private Long seatLayoutId;
    private Long busId;
    private String seatNumber;
    private SeatType seatType;
    private Deck deck;
    private Integer rowNumber;
    private Integer columnNumber;
    private Integer positionX;
    private Integer positionY;
    private Double basePriceMultiplier;
    private Boolean isLadiesSeat;
    private Double price;
}
