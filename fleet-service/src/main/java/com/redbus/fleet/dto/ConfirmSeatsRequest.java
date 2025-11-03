package com.redbus.fleet.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmSeatsRequest {
    
    @NotNull(message = "Trip ID is required")
    @Positive(message = "Trip ID must be positive")
    private Long tripId;
    
    @NotEmpty(message = "At least one seat number is required")
    private List<String> seatNumbers;
    
    @NotNull(message = "From stop sequence is required")
    @Positive(message = "From stop sequence must be positive")
    private Integer fromStopSequence;
    
    @NotNull(message = "To stop sequence is required")
    @Positive(message = "To stop sequence must be positive")
    private Integer toStopSequence;
}
