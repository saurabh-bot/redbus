package com.redbus.search.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBusesRequest {
    
    @NotNull(message = "Source city code is required")
    private String source;
    
    @NotNull(message = "Destination city code is required")
    private String destination;
    
    @NotNull(message = "Travel date is required")
    private LocalDate travelDate;
    
    private Boolean isAc;
    private String busType; // SEATER, SLEEPER
    private String sortBy; // departure (rating removed)
    private Integer page;
    private Integer pageSize;
}
