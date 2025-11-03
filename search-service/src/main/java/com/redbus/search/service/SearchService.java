package com.redbus.search.service;

import com.redbus.search.dto.BusSearchResult;
import com.redbus.search.dto.SearchBusesRequest;
import com.redbus.search.dto.SearchBusesResponse;
import com.redbus.search.model.SearchableRoute;
import com.redbus.search.repository.SearchableRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SearchService {
    
    private final SearchableRouteRepository searchableRouteRepository;
    
    @Cacheable(value = "searchResults", key = "#p0.source + '_' + #p0.destination + '_' + #p0.travelDate + '_' + (#p0.isAc != null ? #p0.isAc : 'null') + '_' + (#p0.busType != null ? #p0.busType : 'null') + '_' + (#p0.sortBy != null ? #p0.sortBy : 'null') + '_' + (#p0.page != null ? #p0.page : '1') + '_' + (#p0.pageSize != null ? #p0.pageSize : '20')")
    public SearchBusesResponse searchBuses(SearchBusesRequest request) {
        log.info("Searching buses from city {} to city {} on {}", 
                request.getSource(), request.getDestination(), request.getTravelDate());
        
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int pageSize = request.getPageSize() != null ? Math.min(request.getPageSize(), 50) : 20;
        
        Pageable pageable;
        
        // Handle sorting (departure, fare)
        if ("departure".equals(request.getSortBy())) {
            pageable = PageRequest.of(page, pageSize, Sort.by("boardingDatetime").ascending());
        } else if ("fare".equals(request.getSortBy())) {
            pageable = PageRequest.of(page, pageSize, Sort.by("fare").ascending());
        } else {
            pageable = PageRequest.of(page, pageSize);
        }
        
        Page<SearchableRoute> results;
        
        // Build query based on filters (using city_code)
        if (request.getIsAc() != null && request.getBusType() != null) {
            results = searchableRouteRepository.findBusesWithAcAndSeatTypeFilter(
                    request.getSource().toUpperCase(), request.getDestination().toUpperCase(), 
                    request.getTravelDate(), request.getIsAc(), request.getBusType(), pageable);
        } else if (request.getIsAc() != null) {
            results = searchableRouteRepository.findBusesWithAcFilter(
                    request.getSource().toUpperCase(), request.getDestination().toUpperCase(), 
                    request.getTravelDate(), request.getIsAc(), pageable);
        } else if (request.getBusType() != null) {
            results = searchableRouteRepository.findBusesWithSeatTypeFilter(
                    request.getSource().toUpperCase(), request.getDestination().toUpperCase(), 
                    request.getTravelDate(), request.getBusType(), pageable);
        } else {
            results = searchableRouteRepository.findAvailableBusesByCityCode(
                    request.getSource().toUpperCase(), request.getDestination().toUpperCase(), 
                    request.getTravelDate(), pageable);
        }
        
        List<BusSearchResult> busResults = results.getContent().stream()
                .map(this::toBusSearchResult)
                .collect(Collectors.toList());
        
        return SearchBusesResponse.builder()
                .total_count(results.getTotalElements())
                .buses(busResults)
                .page(page + 1)
                .page_size(results.getSize())
                .build();
    }
    
    private BusSearchResult toBusSearchResult(SearchableRoute route) {
        List<String> amenities = new java.util.ArrayList<>();
        if (route.getHasWifi() != null && route.getHasWifi()) amenities.add("WiFi");
        if (route.getHasChargingPoint() != null && route.getHasChargingPoint()) amenities.add("Charging");
        if (route.getHasBlanket() != null && route.getHasBlanket()) amenities.add("Blanket");
        if (route.getIsAc() != null && route.getIsAc()) amenities.add("AC");
        
        return BusSearchResult.builder()
                .tripId(route.getTripId())
                .busNumber(extractBusNumber(route.getOtherAttributes()))
                .boardingTime(BusSearchResult.epochToDateTime(route.getBoardingDatetime()))
                .droppingTime(BusSearchResult.epochToDateTime(route.getDroppingDatetime()))
                .fare(route.getFare())
                .amenities(amenities)
                .availableSeats(route.getSeatCount())
                .seatType(route.getSeatType())
                .sourceCityCode(route.getSourceCityCode())
                .destinationCityCode(route.getDestinationCityCode())
                .fromStopSequence(route.getFromStopSequence())
                .toStopSequence(route.getToStopSequence())
                .build();
    }
    
    private String extractBusNumber(String otherAttributes) {
        if (otherAttributes == null || otherAttributes.isEmpty()) {
            return "N/A";
        }
        try {
            // Parse JSON string to extract bus_number
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(otherAttributes);
            String busNumber = jsonNode.has("bus_number") ? jsonNode.get("bus_number").asText() : null;
            return busNumber != null ? busNumber : "N/A";
        } catch (Exception e) {
            log.warn("Failed to extract bus number from otherAttributes: {}", otherAttributes, e);
            return "N/A";
        }
    }
}
