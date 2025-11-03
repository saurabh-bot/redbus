package com.redbus.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmBookingRequest {
    
    @NotBlank(message = "Booking reference ID is required")
    private String bookingReferenceId;
    
    @NotBlank(message = "Payment reference ID is required")
    private String paymentReferenceId;
}
