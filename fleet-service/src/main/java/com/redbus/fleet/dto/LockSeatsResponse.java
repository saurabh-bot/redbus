package com.redbus.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockSeatsResponse {
    private Boolean locked;
    private Long tripId;
    private List<String> lockedSeats;
    private LocalDateTime lockExpiresAt;
    private String message;
}
