package com.redbus.fleet.dto;

import com.redbus.fleet.model.enums.Deck;
import com.redbus.fleet.model.enums.SeatType;
import jakarta.validation.constraints.NotBlank;
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
public class SeatLayoutRequest {
    
    @NotBlank(message = "Seat number is required")
    @jakarta.validation.constraints.Size(max = 10, message = "Seat number must not exceed 10 characters")
    private String seatNumber;
    
    @NotNull(message = "Seat type is required")
    private SeatType seatType;
    
    @NotNull(message = "Deck is required")
    private Deck deck;
    
    @Positive(message = "Row number must be positive")
    private Integer rowNumber;
    
    @Positive(message = "Column number must be positive")
    private Integer columnNumber;
    
    @PositiveOrZero(message = "Position X must be zero or positive")
    private Integer positionX;
    
    @PositiveOrZero(message = "Position Y must be zero or positive")
    private Integer positionY;
    
    @PositiveOrZero(message = "Base price multiplier must be zero or positive")
    private Double basePriceMultiplier;
    
    private Boolean isLadiesSeat;
    
    @PositiveOrZero(message = "Price must be zero or positive")
    private Double price;
}
