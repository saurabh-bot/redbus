package com.redbus.fleet.controller;

import com.redbus.fleet.dto.SeatLayoutListResponse;
import com.redbus.fleet.dto.SeatLayoutRequest;
import com.redbus.fleet.dto.SeatLayoutResponse;
import com.redbus.fleet.service.SeatLayoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buses/{busId}/seat-layout")
@RequiredArgsConstructor
@Tag(name = "Seat Layout Management", description = "APIs for managing seat layouts")
public class SeatLayoutController {
    
    private final SeatLayoutService seatLayoutService;
    
    @PostMapping
    @Operation(summary = "Create seat layout", description = "Creates seat layout for a bus")
    public ResponseEntity<SeatLayoutListResponse> createSeatLayout(
            @PathVariable("busId") Long busId,
            @Valid @RequestBody List<SeatLayoutRequest> requests) {
        SeatLayoutListResponse response = seatLayoutService.createSeatLayout(busId, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get seat layout", description = "Retrieves seat layout for a bus")
    public ResponseEntity<SeatLayoutListResponse> getSeatLayout(@PathVariable("busId") Long busId) {
        SeatLayoutListResponse response = seatLayoutService.getSeatLayout(busId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{seatLayoutId}")
    @Operation(summary = "Update seat layout", description = "Updates a specific seat in the layout")
    public ResponseEntity<SeatLayoutResponse> updateSeatLayout(
            @PathVariable("busId") Long busId,
            @PathVariable("seatLayoutId") Long seatLayoutId,
            @Valid @RequestBody SeatLayoutRequest request) {
        SeatLayoutResponse response = seatLayoutService.updateSeatLayout(busId, seatLayoutId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping
    @Operation(summary = "Delete all seat layouts", description = "Deletes all seat layouts for a bus")
    public ResponseEntity<?> deleteAllSeatLayouts(@PathVariable("busId") Long busId) {
        seatLayoutService.deleteAllSeatsForBus(busId);
        return ResponseEntity.ok("All seat layouts deleted successfully for bus " + busId);
    }
}
