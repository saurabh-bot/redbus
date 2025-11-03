package com.redbus.booking.dto;

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
public class CreateBookingRequest {
    
    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;
    
    @NotNull(message = "Trip ID is required")
    @Positive(message = "Trip ID must be positive")
    private Long tripId;
    
    @NotNull(message = "Boarding city name is required")
    private String boardingCityName;
    
    @NotNull(message = "Dropping city name is required")
    private String droppingCityName;
    
    @NotNull(message = "Boarding stop sequence is required")
    @Positive(message = "Boarding stop sequence must be positive")
    private Integer boardingStopSequence;
    
    @NotNull(message = "Dropping stop sequence is required")
    @Positive(message = "Dropping stop sequence must be positive")
    private Integer droppingStopSequence;
    
    @NotNull(message = "Boarding datetime is required")
    private String boardingDatetime; // ISO format
    
    @NotNull(message = "Dropping datetime is required")
    private String droppingDatetime; // ISO format
    
    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    private Double totalAmount;
    
    @NotNull(message = "At least one passenger is required")
    @NotEmpty(message = "At least one passenger is required")
    @jakarta.validation.Valid
    private List<PassengerRequest> passengers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerRequest {
        @NotNull(message = "Seat number is required")
        private String seatNumber;
        
        @NotNull(message = "Name is required")
        private String name;
        
        private Integer age;
        private String gender;
        private String email;
        private String phoneNumber;
    }
}
