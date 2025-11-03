package com.redbus.fleet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripInstanceRequest {
    
    @NotNull(message = "Route ID is required")
    private Long routeId;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
}

