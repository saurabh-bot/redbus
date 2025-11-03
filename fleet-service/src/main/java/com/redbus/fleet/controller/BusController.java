package com.redbus.fleet.controller;

import com.redbus.fleet.dto.BusListResponse;
import com.redbus.fleet.dto.BusRequest;
import com.redbus.fleet.dto.BusResponse;
import com.redbus.fleet.model.enums.BusType;
import com.redbus.fleet.service.BusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buses")
@RequiredArgsConstructor
@Tag(name = "Bus Management", description = "APIs for managing buses")
public class BusController {
    
    private final BusService busService;
    
    @PostMapping
    @Operation(summary = "Create a new bus", description = "Creates a new bus with provided details")
    public ResponseEntity<BusResponse> createBus(@Valid @RequestBody BusRequest request) {
        BusResponse response = busService.createBus(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{busId}")
    @Operation(summary = "Get bus by ID", description = "Retrieves bus details by bus ID")
    public ResponseEntity<BusResponse> getBus(@PathVariable("busId") Long busId) {
        BusResponse response = busService.getBusById(busId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{busId}")
    @Operation(summary = "Update bus", description = "Updates existing bus details")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable("busId") Long busId,
            @Valid @RequestBody BusRequest request) {
        BusResponse response = busService.updateBus(busId, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "List buses", description = "Retrieves buses with optional filters and pagination")
    public ResponseEntity<BusListResponse> listBuses(
            @RequestParam(value = "operator_id", required = false) Long operatorId,
            @RequestParam(value = "is_active", required = false) Boolean isActive,
            @RequestParam(value = "seat_type", required = false) BusType seatType,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {
        BusListResponse response = busService.listBuses(operatorId, isActive, seatType, page, pageSize);
        return ResponseEntity.ok(response);
    }
}
