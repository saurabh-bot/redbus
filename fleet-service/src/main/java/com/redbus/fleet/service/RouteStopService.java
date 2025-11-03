package com.redbus.fleet.service;

import com.redbus.fleet.dto.RouteStopRequest;
import com.redbus.fleet.dto.RouteStopResponse;
import com.redbus.fleet.dto.RouteStopsListResponse;
import com.redbus.fleet.mapper.RouteStopMapper;
import com.redbus.fleet.model.City;
import com.redbus.fleet.model.Route;
import com.redbus.fleet.model.RouteStop;
import com.redbus.fleet.repository.CityRepository;
import com.redbus.fleet.repository.RouteRepository;
import com.redbus.fleet.repository.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RouteStopService {
    
    private final RouteStopRepository routeStopRepository;
    private final RouteRepository routeRepository;
    private final CityRepository cityRepository;
    private final RouteStopMapper routeStopMapper;
    
    public RouteStopResponse createRouteStop(Long routeId, RouteStopRequest request) {
        log.info("Creating route stop for route ID: {} at sequence: {}", routeId, request.getSequence());
        
        // Verify route exists
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        
        // Verify city exists by code
        City city = cityRepository.findByCityCode(request.getCityCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("City not found with code: " + request.getCityCode()));
        
        // Check if sequence already exists for this route
        routeStopRepository.findByRouteIdAndSequence(routeId, request.getSequence())
                .ifPresent(stop -> {
                    throw new IllegalArgumentException("Route stop with sequence " + request.getSequence() + " already exists for route " + routeId);
                });
        
        RouteStop routeStop = routeStopMapper.toEntity(request);
        routeStop.setRouteId(routeId);
        routeStop.setCityId(city.getCityId()); // Set city ID internally
        RouteStop savedStop = routeStopRepository.save(routeStop);
        
        log.info("Route stop created successfully with ID: {}", savedStop.getRouteStopId());
        return routeStopMapper.toResponseWithDetails(savedStop, city);
    }
    
    @Transactional(readOnly = true)
    public RouteStopsListResponse getRouteStops(Long routeId) {
        log.info("Fetching all stops for route ID: {}", routeId);
        
        // Verify route exists
        routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        
        List<RouteStop> stops = routeStopRepository.findByRouteIdOrderBySequenceAsc(routeId);
        List<RouteStopResponse> stopResponses = stops.stream()
                .map(stop -> {
                    City city = cityRepository.findById(stop.getCityId())
                            .orElse(null);
                    return routeStopMapper.toResponseWithDetails(stop, city);
                })
                .collect(Collectors.toList());
        
        return RouteStopsListResponse.builder()
                .routeId(routeId)
                .stops(stopResponses)
                .build();
    }
    
    public RouteStopResponse updateRouteStop(Long routeId, Long routeStopId, RouteStopRequest request) {
        log.info("Updating route stop ID: {} for route ID: {}", routeStopId, routeId);
        
        RouteStop routeStop = routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new IllegalArgumentException("Route stop not found with ID: " + routeStopId));
        
        if (!routeStop.getRouteId().equals(routeId)) {
            throw new IllegalArgumentException("Route stop does not belong to route " + routeId);
        }
        
        // Check sequence conflict if sequence is being changed
        if (request.getSequence() != null && !request.getSequence().equals(routeStop.getSequence())) {
            routeStopRepository.findByRouteIdAndSequence(routeId, request.getSequence())
                    .ifPresent(existingStop -> {
                        if (!existingStop.getRouteStopId().equals(routeStopId)) {
                            throw new IllegalArgumentException("Route stop with sequence " + request.getSequence() + " already exists for route " + routeId);
                        }
                    });
        }
        
        // Handle city code update
        if (request.getCityCode() != null) {
            City city = cityRepository.findByCityCode(request.getCityCode().toUpperCase())
                    .orElseThrow(() -> new IllegalArgumentException("City not found with code: " + request.getCityCode()));
            routeStop.setCityId(city.getCityId());
        }
        
        // Update other fields
        routeStopMapper.updateEntityFromRequest(request, routeStop);
        RouteStop updatedStop = routeStopRepository.save(routeStop);
        
        City city = cityRepository.findById(updatedStop.getCityId()).orElse(null);
        log.info("Route stop updated successfully with ID: {}", updatedStop.getRouteStopId());
        return routeStopMapper.toResponseWithDetails(updatedStop, city);
    }
    
    public void deleteRouteStop(Long routeId, Long routeStopId) {
        log.info("Deleting route stop ID: {} for route ID: {}", routeStopId, routeId);
        
        RouteStop routeStop = routeStopRepository.findById(routeStopId)
                .orElseThrow(() -> new IllegalArgumentException("Route stop not found with ID: " + routeStopId));
        
        if (!routeStop.getRouteId().equals(routeId)) {
            throw new IllegalArgumentException("Route stop does not belong to route " + routeId);
        }
        
        routeStopRepository.delete(routeStop);
        log.info("Route stop deleted successfully");
    }
}
