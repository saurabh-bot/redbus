package com.redbus.search.service;

import com.redbus.search.model.SearchableRoute;
import com.redbus.search.repository.SearchableRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class SearchableRouteService {
    
    private final SearchableRouteRepository searchableRouteRepository;
    private final RestTemplate restTemplate;
    
    private static final String FLEET_SERVICE_URL = "http://localhost:8081/api/v1";
    
    public SearchableRouteService(SearchableRouteRepository searchableRouteRepository, RestTemplate restTemplate) {
        this.searchableRouteRepository = searchableRouteRepository;
        this.restTemplate = restTemplate;
    }
    
    /**
     * Populates searchable routes for all trips of a given route.
     * Creates entries for all possible source-destination combinations from route stops.
     */
    public void populateSearchableRoutesForRoute(Long routeId) {
        log.info("Populating searchable routes for route ID: {}", routeId);
        
        try {
            // First, delete all existing searchable routes for trips of this route
            // Get all trips for this route and delete their searchable routes
            String tripsUrl = FLEET_SERVICE_URL + "/trip-instances/route/" + routeId;
            ResponseEntity<List> tripsResponse = restTemplate.getForEntity(tripsUrl, List.class);
            List<Map<String, Object>> existingTrips = tripsResponse.getBody();
            
            if (existingTrips != null && !existingTrips.isEmpty()) {
                int deletedCount = 0;
                for (Map<String, Object> trip : existingTrips) {
                    Long tripId = Long.valueOf(trip.get("trip_id").toString());
                    List<SearchableRoute> existingRoutes = searchableRouteRepository.findByTripId(tripId);
                    if (!existingRoutes.isEmpty()) {
                        searchableRouteRepository.deleteByTripId(tripId);
                        deletedCount += existingRoutes.size();
                    }
                }
                log.info("Deleted {} existing searchable route records before repopulating", deletedCount);
            }
            
            // Fetch trips for the route (reuse the response we already have)
            List<Map<String, Object>> trips = existingTrips;
            
            if (trips == null || trips.isEmpty()) {
                log.warn("No trips found for route {}", routeId);
                return;
            }
            
            // Fetch route stops
            String stopsUrl = FLEET_SERVICE_URL + "/routes/" + routeId + "/stops";
            ResponseEntity<Map> stopsResponse = restTemplate.getForEntity(stopsUrl, Map.class);
            Map<String, Object> stopsData = stopsResponse.getBody();
            List<Map<String, Object>> stops = (List<Map<String, Object>>) stopsData.get("stops");
            
            if (stops == null || stops.isEmpty()) {
                log.warn("No stops found for route {}", routeId);
                return;
            }
            
            // Fetch route details
            String routeUrl = FLEET_SERVICE_URL + "/routes/" + routeId;
            ResponseEntity<Map> routeResponse = restTemplate.getForEntity(routeUrl, Map.class);
            Map<String, Object> route = routeResponse.getBody();
            Long busId = Long.valueOf(route.get("bus_id").toString());
            
            // Fetch bus details
            String busUrl = FLEET_SERVICE_URL + "/buses/" + busId;
            ResponseEntity<Map> busResponse = restTemplate.getForEntity(busUrl, Map.class);
            Map<String, Object> bus = busResponse.getBody();
            
            String seatType = bus.get("seat_type").toString();
            Boolean isAc = Boolean.valueOf(bus.get("is_ac").toString());
            Integer totalSeats = Integer.valueOf(bus.get("total_seats").toString());
            Map<String, Object> amenities = (Map<String, Object>) bus.get("amenities");
            Boolean hasWifi = amenities != null && Boolean.valueOf(amenities.getOrDefault("wifi", false).toString());
            Boolean hasCharging = amenities != null && Boolean.valueOf(amenities.getOrDefault("charging_point", false).toString());
            Boolean hasBlanket = amenities != null && Boolean.valueOf(amenities.getOrDefault("blanket", false).toString());
            String busNumber = bus.get("bus_number").toString();
            
            // Generate all source-destination combinations
            List<SearchableRoute> searchableRoutes = new ArrayList<>();
            int combinations = 0;
            
            for (int i = 0; i < stops.size(); i++) {
                for (int j = i + 1; j < stops.size(); j++) {
                    Map<String, Object> sourceStop = stops.get(i);
                    Map<String, Object> destStop = stops.get(j);
                    
                    Boolean sourceBoarding = Boolean.valueOf(sourceStop.getOrDefault("is_boarding_point", true).toString());
                    Boolean destDropping = Boolean.valueOf(destStop.getOrDefault("is_dropping_point", true).toString());
                    
                    if (sourceBoarding && destDropping) {
                        combinations++;
                    }
                }
            }
            
            log.info("Found {} valid source-destination combinations for route {}", combinations, routeId);
            
            // For each trip, create searchable routes
            for (Map<String, Object> trip : trips) {
                Long tripId = Long.valueOf(trip.get("trip_id").toString());
                String travelDate = trip.get("date").toString();
                Object startEpochObj = trip.get("start_datetime_epoch");
                
                Long startEpoch;
                if (startEpochObj == null) {
                    // If start_datetime_epoch is not set, calculate default based on trip date
                    // Default: 10:00 AM UTC on the trip date
                    try {
                        java.time.LocalDate tripLocalDate = java.time.LocalDate.parse(travelDate);
                        java.time.LocalDateTime tripDateTime = tripLocalDate.atTime(10, 0, 0);
                        java.time.ZonedDateTime tripZonedDateTime = tripDateTime.atZone(java.time.ZoneOffset.UTC);
                        startEpoch = tripZonedDateTime.toEpochSecond();
                        log.info("Trip {} has no start_datetime_epoch, using default 10:00 AM UTC on {}", tripId, travelDate);
                    } catch (Exception e) {
                        log.warn("Skipping trip {} - could not parse date {}: {}", tripId, travelDate, e.getMessage());
                        continue;
                    }
                } else {
                    startEpoch = Long.valueOf(startEpochObj.toString());
                }
                
                // Create searchable routes for all combinations
                for (int i = 0; i < stops.size(); i++) {
                    for (int j = i + 1; j < stops.size(); j++) {
                        Map<String, Object> sourceStop = stops.get(i);
                        Map<String, Object> destStop = stops.get(j);
                        
                        Boolean sourceBoarding = Boolean.valueOf(sourceStop.getOrDefault("is_boarding_point", true).toString());
                        Boolean destDropping = Boolean.valueOf(destStop.getOrDefault("is_dropping_point", true).toString());
                        
                        if (!sourceBoarding || !destDropping) {
                            continue;
                        }
                        
                        // Get city IDs and codes
                        Long sourceCityId = getLongValue(sourceStop.get("city_id"));
                        Long destCityId = getLongValue(destStop.get("city_id"));
                        String sourceCityCode = sourceStop.get("city_code").toString().toUpperCase();
                        String destCityCode = destStop.get("city_code").toString().toUpperCase();
                        
                        // Get stop sequences
                        Integer fromStopSequence = getIntegerValue(sourceStop.get("sequence"), i + 1);
                        Integer toStopSequence = getIntegerValue(destStop.get("sequence"), j + 1);
                        
                        if (sourceCityId == null || destCityId == null || sourceCityCode == null || destCityCode == null) {
                            log.warn("Could not find city IDs or codes for {} or {}", sourceCityCode, destCityCode);
                            continue;
                        }
                        
                        Integer departureOffset = getIntegerValue(sourceStop.get("departure_offset_minutes"), 0);
                        Integer arrivalOffset = getIntegerValue(destStop.get("arrival_offset_minutes"), 0);
                        
                        Double sourceFare = getDoubleValue(sourceStop.get("fare_from_start"), 0.0);
                        Double destFare = getDoubleValue(destStop.get("fare_from_start"), 0.0);
                        Double fare = destFare - sourceFare;
                        if (fare <= 0) {
                            fare = Math.abs(sourceFare) + 100; // Fallback calculation
                        }
                        
                        Long boardingEpoch = startEpoch + (departureOffset * 60L);
                        Long droppingEpoch = startEpoch + (arrivalOffset * 60L);
                        
                        // Calculate travel_date based on boarding/departure date
                        // travel_date = date when passenger boards the bus (not arrival date)
                        java.time.Instant boardingInstant = java.time.Instant.ofEpochSecond(boardingEpoch);
                        java.time.LocalDate actualTravelDate = boardingInstant.atZone(java.time.ZoneOffset.UTC).toLocalDate();
                        
                        String otherAttributes = String.format("{\"bus_number\": \"%s\"}", busNumber);
                        
                        // Check if already exists (by city codes and travel_date)
                        // Note: We allow duplicate trip+city combinations with different travel_dates
                        // if the same trip has multiple segments that board on different dates
                        List<SearchableRoute> existing = searchableRouteRepository.findByTripId(tripId);
                        boolean exists = existing.stream().anyMatch(sr ->
                                sourceCityCode.equals(sr.getSourceCityCode()) &&
                                destCityCode.equals(sr.getDestinationCityCode()) &&
                                actualTravelDate.equals(sr.getTravelDate()));
                        
                        if (exists) {
                            continue;
                        }
                        
                        SearchableRoute searchableRoute = SearchableRoute.builder()
                                .tripId(tripId)
                                .isActive(true)
                                .routeId(routeId)
                                .sourceCityId(sourceCityId)
                                .destinationCityId(destCityId)
                                .sourceCityCode(sourceCityCode)
                                .destinationCityCode(destCityCode)
                                .fromStopSequence(fromStopSequence)
                                .toStopSequence(toStopSequence)
                                .boardingDatetime(boardingEpoch)
                                .droppingDatetime(droppingEpoch)
                                .travelDate(actualTravelDate) // Use destination arrival date
                                .seatCount(totalSeats)
                                .seatType(seatType)
                                .busId(busId)
                                .fare(fare)
                                .hasWifi(hasWifi)
                                .hasChargingPoint(hasCharging)
                                .hasBlanket(hasBlanket)
                                .isAc(isAc)
                                .otherAttributes(otherAttributes)
                                .build();
                        
                        searchableRoutes.add(searchableRoute);
                    }
                }
            }
            
            // Bulk save
            if (!searchableRoutes.isEmpty()) {
                searchableRouteRepository.saveAll(searchableRoutes);
                log.info("Created {} searchable route records", searchableRoutes.size());
            } else {
                log.warn("No searchable routes to create");
            }
            
        } catch (Exception e) {
            log.error("Error populating searchable routes for route {}", routeId, e);
            throw new RuntimeException("Failed to populate searchable routes", e);
        }
    }
    
    private Long getLongValue(Object value) {
        if (value == null) return null;
        try {
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
            return Long.valueOf(value.toString());
        } catch (Exception e) {
            return null;
        }
    }
    
    private Integer getIntegerValue(Object value, Integer defaultValue) {
        if (value == null) return defaultValue;
        try {
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    private Double getDoubleValue(Object value, Double defaultValue) {
        if (value == null) return defaultValue;
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.valueOf(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}

