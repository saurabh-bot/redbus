package com.redbus.fleet.controller;

import com.redbus.fleet.dto.TripInstanceRequest;
import com.redbus.fleet.model.TripInstance;
import com.redbus.fleet.service.TripInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trip-instances")
@RequiredArgsConstructor
@Tag(name = "Trip Instance Management", description = "APIs for managing trip instances")
public class TripInstanceController {
    
    private final TripInstanceService tripInstanceService;
    
    @PostMapping("/generate")
    @Operation(summary = "Generate trip instances", description = "Manually triggers generation of trip instances for next 180 days")
    public ResponseEntity<?> generateTripInstances() {
        tripInstanceService.generateTripInstancesForNext180Days();
        return ResponseEntity.ok("Trip instances generation completed successfully");
    }
    
    @PostMapping
    @Operation(summary = "Create trip instance", description = "Creates a single trip instance")
    public ResponseEntity<TripInstance> createTripInstance(
            @Valid @RequestBody TripInstanceRequest request) {
        TripInstance trip = tripInstanceService.createTripInstance(request.getRouteId(), request.getDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(trip);
    }
    
    @GetMapping("/route/{routeId}")
    @Operation(summary = "Get trips by route", description = "Retrieves all trip instances for a route")
    public ResponseEntity<List<TripInstance>> getTripsByRoute(@PathVariable("routeId") Long routeId) {
        List<TripInstance> trips = tripInstanceService.getTripsByRoute(routeId);
        return ResponseEntity.ok(trips);
    }
    
    @GetMapping("/{tripId}")
    @Operation(summary = "Get trip by ID", description = "Retrieves trip instance by ID")
    public ResponseEntity<TripInstance> getTripById(@PathVariable("tripId") Long tripId) {
        TripInstance trip = tripInstanceService.getTripById(tripId);
        return ResponseEntity.ok(trip);
    }
    
}
