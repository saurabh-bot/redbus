package com.redbus.booking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {
    
    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerRequest> passengers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerRequest {
        private String seatNumber;
        private String name;
        private Integer age;
        private String gender;
        private String email;
        private String phoneNumber;
    }
}
