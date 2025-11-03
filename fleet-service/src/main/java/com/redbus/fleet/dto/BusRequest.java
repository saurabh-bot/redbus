package com.redbus.fleet.dto;

import com.redbus.fleet.model.enums.BusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRequest {
    
    @NotNull(message = "Operator ID is required")
    @Positive(message = "Operator ID must be positive")
    private Long operatorId;
    
    @NotBlank(message = "Bus number is required")
    @Size(max = 50, message = "Bus number must not exceed 50 characters")
    private String busNumber;
    
    @NotNull(message = "Seat type is required")
    private BusType seatType;
    
    private Boolean isActive;
    
    private Boolean isAc;
    
    private Map<String, Object> amenities; // JSON structure for amenities
    
    @Positive(message = "Total seats must be positive")
    private Integer totalSeats;
}
