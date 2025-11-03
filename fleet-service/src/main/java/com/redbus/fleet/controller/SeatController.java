package com.redbus.fleet.controller;

import com.redbus.fleet.dto.CancelSeatsRequest;
import com.redbus.fleet.dto.ConfirmSeatsRequest;
import com.redbus.fleet.dto.LockSeatsRequest;
import com.redbus.fleet.dto.LockSeatsResponse;
import com.redbus.fleet.dto.ReleaseSeatsRequest;
import com.redbus.fleet.dto.SeatAvailabilityResponse;
import com.redbus.fleet.service.SeatAvailabilityService;
import com.redbus.fleet.service.SeatLockService;
import com.redbus.fleet.service.SeatReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
@Tag(name = "Seat Management", description = "APIs for seat locking, availability, and booking")
public class SeatController {
    
    private final SeatLockService seatLockService;
    private final SeatAvailabilityService seatAvailabilityService;
    private final SeatReservationService seatReservationService;
    
    @PostMapping("/lock")
    @Operation(summary = "Lock seats", description = "Locks seats with 15-minute TTL")
    public ResponseEntity<LockSeatsResponse> lockSeats(@Valid @RequestBody LockSeatsRequest request) {
        seatLockService.lockSeats(
                request.getTripId(),
                request.getSeatNumbers(),
                request.getFromStopSequence(),
                request.getToStopSequence()
        );
        
        LocalDateTime expiresAt = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(15);
        
        LockSeatsResponse response = LockSeatsResponse.builder()
                .locked(true)
                .tripId(request.getTripId())
                .lockedSeats(request.getSeatNumbers())
                .lockExpiresAt(expiresAt)
                .message("Seats locked successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/confirm")
    @Operation(summary = "Confirm seats", description = "Confirms seat reservations (moves from LOCKED to CONFIRMED)")
    public ResponseEntity<?> confirmSeats(@Valid @RequestBody ConfirmSeatsRequest request) {
        seatReservationService.confirmSeats(
                request.getTripId(),
                request.getSeatNumbers(),
                request.getFromStopSequence(),
                request.getToStopSequence()
        );
        return ResponseEntity.ok(Map.of(
                "confirmed", true,
                "trip_id", request.getTripId(),
                "seats", request.getSeatNumbers(),
                "message", "Seats confirmed successfully"
        ));
    }
    
    @PostMapping("/release")
    @Operation(summary = "Release seats", description = "Releases locked seats")
    public ResponseEntity<?> releaseSeats(@Valid @RequestBody ReleaseSeatsRequest request) {
        seatLockService.releaseSeats(
                request.getTripId(),
                request.getSeatNumbers(),
                request.getFromStopSequence(),
                request.getToStopSequence()
        );
        return ResponseEntity.ok(Map.of(
                "released", true,
                "trip_id", request.getTripId(),
                "seats", request.getSeatNumbers(),
                "message", "Seats released successfully"
        ));
    }
    
    @PostMapping("/cancel")
    @Operation(summary = "Cancel seats", description = "Cancels seat reservations by marking them as CANCELLED")
    public ResponseEntity<?> cancelSeats(@Valid @RequestBody CancelSeatsRequest request) {
        seatReservationService.cancelSeats(
                request.getTripId(),
                request.getSeatNumbers(),
                request.getFromStopSequence(),
                request.getToStopSequence()
        );
        return ResponseEntity.ok(Map.of(
                "cancelled", true,
                "trip_id", request.getTripId(),
                "seats", request.getSeatNumbers(),
                "message", "Seats cancelled successfully"
        ));
    }
    
    @GetMapping("/availability")
    @Operation(summary = "Get seat availability", description = "Retrieves seat availability for a trip and route segment")
    public ResponseEntity<SeatAvailabilityResponse> getSeatAvailability(
            @RequestParam(value = "trip_id") @NotNull Long tripId,
            @RequestParam(value = "from_stop_sequence") @NotNull Integer fromStopSequence,
            @RequestParam(value = "to_stop_sequence") @NotNull Integer toStopSequence) {
        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(
                tripId, fromStopSequence, toStopSequence);
        return ResponseEntity.ok(response);
    }
}
