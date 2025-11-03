package com.redbus.search.controller;

import com.redbus.search.service.SearchableRouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/searchable-routes")
@RequiredArgsConstructor
@Tag(name = "Searchable Route Management", description = "APIs for managing searchable routes")
public class SearchableRouteController {
    
    private final SearchableRouteService searchableRouteService;
    
    @PostMapping("/populate/{routeId}")
    @Operation(summary = "Populate searchable routes", description = "Populates searchable routes for all trips of a given route")
    public ResponseEntity<?> populateSearchableRoutes(@PathVariable("routeId") Long routeId) {
        searchableRouteService.populateSearchableRoutesForRoute(routeId);
        return ResponseEntity.ok("Searchable routes populated successfully for route " + routeId);
    }
}

