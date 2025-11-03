package com.redbus.gateway.controller;

import com.redbus.gateway.config.FleetServiceConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seats")
@Tag(name = "Seat Gateway", description = "Gateway API for seat operations")
public class SeatController {

    private final FleetServiceConfig fleetServiceConfig;
    private final RestTemplate restTemplate;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SeatController.class);
    
    public SeatController(FleetServiceConfig fleetServiceConfig, RestTemplate restTemplate) {
        this.fleetServiceConfig = fleetServiceConfig;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/availability")
    @Operation(summary = "Get seat availability", description = "Get seat availability via gateway (proxies to fleet service)")
    public ResponseEntity<?> getSeatAvailability(
            @RequestParam("trip_id") Long tripId,
            @RequestParam("from_stop_sequence") Integer fromStopSequence,
            @RequestParam("to_stop_sequence") Integer toStopSequence) {
        
        String fleetServiceUrl = fleetServiceConfig.getServiceUrl()
                + "/api/v1/seats/availability"
                + "?trip_id=" + tripId
                + "&from_stop_sequence=" + fromStopSequence
                + "&to_stop_sequence=" + toStopSequence;
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(fleetServiceUrl, Map.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Failed to get seat availability via gateway. Trip: {}, {}->{}", tripId, fromStopSequence, toStopSequence, e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "Failed to communicate with fleet service: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/lock")
    @Operation(summary = "Lock seats", description = "Lock seats via gateway (proxies to fleet service)")
    public ResponseEntity<?> lockSeats(@RequestBody Map<String, Object> request) {
        Map<String, Object> cleanRequest = new java.util.HashMap<>(request);
        cleanRequest.remove("user_id");
        
        String fleetServiceUrl = fleetServiceConfig.getServiceUrl() + "/api/v1/seats/lock";
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(cleanRequest, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    fleetServiceUrl, 
                    HttpMethod.POST, 
                    httpEntity, 
                    Map.class);
            
            log.info("Seats locked via gateway. Trip: {}, Seats: {}", request.get("trip_id"), request.get("seat_numbers"));
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Failed to lock seats via gateway. Trip: {}, Seats: {}", request.get("trip_id"), request.get("seat_numbers"), e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "Failed to communicate with fleet service: " + e.getMessage()
            ));
        }
    }
}

