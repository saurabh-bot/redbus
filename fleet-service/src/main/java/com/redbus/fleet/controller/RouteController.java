package com.redbus.fleet.controller;

import com.redbus.fleet.dto.RouteRequest;
import com.redbus.fleet.dto.RouteResponse;
import com.redbus.fleet.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Tag(name = "Route Management", description = "APIs for managing routes")
public class RouteController {
    
    private final RouteService routeService;
    
    @PostMapping
    @Operation(summary = "Create a new route", description = "Creates a new route for a bus")
    public ResponseEntity<RouteResponse> createRoute(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.createRoute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{routeId}")
    @Operation(summary = "Get route by ID", description = "Retrieves route details by route ID")
    public ResponseEntity<RouteResponse> getRoute(@PathVariable("routeId") Long routeId) {
        RouteResponse response = routeService.getRouteById(routeId);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{routeId}")
    @Operation(summary = "Update route", description = "Updates existing route details")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable("routeId") Long routeId,
            @Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeService.updateRoute(routeId, request);
        return ResponseEntity.ok(response);
    }
}
