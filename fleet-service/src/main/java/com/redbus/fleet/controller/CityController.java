package com.redbus.fleet.controller;

import com.redbus.fleet.dto.CityListResponse;
import com.redbus.fleet.dto.CityRequest;
import com.redbus.fleet.dto.CityResponse;
import com.redbus.fleet.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
@RequiredArgsConstructor
@Tag(name = "City Management", description = "APIs for managing cities")
public class CityController {
    
    private final CityService cityService;
    
    @PostMapping
    @Operation(summary = "Create a new city", description = "Creates a new city with provided details")
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityRequest request) {
        CityResponse response = cityService.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{cityCode}")
    @Operation(summary = "Get city by code", description = "Retrieves city details by city code (e.g., DEL, MUM, BLR)")
    public ResponseEntity<CityResponse> getCity(@PathVariable("cityCode") String cityCode) {
        CityResponse response = cityService.getCityByCode(cityCode);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{cityCode}")
    @Operation(summary = "Update city", description = "Updates existing city details using city code")
    public ResponseEntity<CityResponse> updateCity(
            @PathVariable("cityCode") String cityCode,
            @Valid @RequestBody CityRequest request) {
        CityResponse response = cityService.updateCity(cityCode, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "List all cities", description = "Retrieves all cities with total count")
    public ResponseEntity<CityListResponse> getAllCities() {
        CityListResponse response = cityService.getAllCities();
        return ResponseEntity.ok(response);
    }
}
