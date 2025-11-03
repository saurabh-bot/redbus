package com.redbus.fleet.controller;

import com.redbus.fleet.dto.RouteStopRequest;
import com.redbus.fleet.dto.RouteStopResponse;
import com.redbus.fleet.dto.RouteStopsListResponse;
import com.redbus.fleet.service.RouteStopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes/{routeId}/stops")
@RequiredArgsConstructor
@Tag(name = "Route Stop Management", description = "APIs for managing route stops")
public class RouteStopController {
    
    private final RouteStopService routeStopService;
    
    @PostMapping
    @Operation(summary = "Create a new route stop", description = "Adds a stop to a route")
    public ResponseEntity<RouteStopResponse> createRouteStop(
            @PathVariable("routeId") Long routeId,
            @Valid @RequestBody RouteStopRequest request) {
        RouteStopResponse response = routeStopService.createRouteStop(routeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all route stops", description = "Retrieves all stops for a route ordered by sequence")
    public ResponseEntity<RouteStopsListResponse> getRouteStops(@PathVariable("routeId") Long routeId) {
        RouteStopsListResponse response = routeStopService.getRouteStops(routeId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{routeStopId}")
    @Operation(summary = "Update route stop", description = "Updates existing route stop details")
    public ResponseEntity<RouteStopResponse> updateRouteStop(
            @PathVariable("routeId") Long routeId,
            @PathVariable("routeStopId") Long routeStopId,
            @Valid @RequestBody RouteStopRequest request) {
        RouteStopResponse response = routeStopService.updateRouteStop(routeId, routeStopId, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{routeStopId}")
    @Operation(summary = "Delete route stop", description = "Deletes a route stop")
    public ResponseEntity<Void> deleteRouteStop(
            @PathVariable("routeId") Long routeId,
            @PathVariable("routeStopId") Long routeStopId) {
        routeStopService.deleteRouteStop(routeId, routeStopId);
        return ResponseEntity.noContent().build();
    }
}
