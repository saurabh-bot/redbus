package com.redbus.gateway.controller;

import com.redbus.gateway.config.SearchServiceConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search Gateway", description = "Gateway API for searching buses")
public class SearchController {

    private final SearchServiceConfig searchServiceConfig;
    private final RestTemplate restTemplate;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SearchController.class);
    
    public SearchController(SearchServiceConfig searchServiceConfig, RestTemplate restTemplate) {
        this.searchServiceConfig = searchServiceConfig;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/buses")
    @Operation(summary = "Search buses", description = "Search available buses via gateway (proxies to search service)")
    public ResponseEntity<?> searchBuses(
            @RequestParam("source") String source,
            @RequestParam("destination") String destination,
            @RequestParam("travel_date") LocalDate travelDate,
            @RequestParam(value = "is_ac", required = false) Boolean isAc,
            @RequestParam(value = "bus_type", required = false) String busType,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer pageSize) {
        
        StringBuilder urlBuilder = new StringBuilder(searchServiceConfig.getUrl())
                .append("/api/v1/search/buses")
                .append("?source=").append(source)
                .append("&destination=").append(destination)
                .append("&travel_date=").append(travelDate);
        
        if (isAc != null) {
            urlBuilder.append("&is_ac=").append(isAc);
        }
        if (busType != null) {
            urlBuilder.append("&bus_type=").append(busType);
        }
        if (sortBy != null) {
            urlBuilder.append("&sort_by=").append(sortBy);
        }
        if (page != null) {
            urlBuilder.append("&page=").append(page);
        }
        if (pageSize != null) {
            urlBuilder.append("&page_size=").append(pageSize);
        }
        
        String searchServiceUrl = urlBuilder.toString();
        
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(searchServiceUrl, Map.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Failed to search buses via gateway. Source: {}, Destination: {}, Date: {}", source, destination, travelDate, e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "Failed to communicate with search service: " + e.getMessage()
            ));
        }
    }
}
