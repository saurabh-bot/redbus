package com.redbus.fleet.service;

import com.redbus.fleet.dto.RouteRequest;
import com.redbus.fleet.dto.RouteResponse;
import com.redbus.fleet.mapper.RouteMapper;
import com.redbus.fleet.model.City;
import com.redbus.fleet.model.Route;
import com.redbus.fleet.repository.CityRepository;
import com.redbus.fleet.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RouteService {
    
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final CityRepository cityRepository;
    
    public RouteResponse createRoute(RouteRequest request) {
        log.info("Creating route from city {} to city {}", request.getSourceCityCode(), request.getDestinationCityCode());
        
        // Get cities by code
        City sourceCity = cityRepository.findByCityCode(request.getSourceCityCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Source city not found with code: " + request.getSourceCityCode()));
        
        City destCity = cityRepository.findByCityCode(request.getDestinationCityCode().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Destination city not found with code: " + request.getDestinationCityCode()));
        
        if (sourceCity.getCityId().equals(destCity.getCityId())) {
            throw new IllegalArgumentException("Source and destination cities cannot be the same");
        }
        
        // Create route with city IDs (internal use)
        Route route = Route.builder()
                .busId(request.getBusId())
                .sourceCityId(sourceCity.getCityId())
                .destinationCityId(destCity.getCityId())
                .distanceKm(request.getDistanceKm())
                .estimatedDurationMinutes(request.getEstimatedDurationMinutes())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        
        Route savedRoute = routeRepository.save(route);
        log.info("Route created successfully with ID: {}", savedRoute.getRouteId());
        return routeMapper.toResponseWithCityCodes(savedRoute, cityRepository);
    }
    
    @Transactional(readOnly = true)
    public RouteResponse getRouteById(Long routeId) {
        log.info("Fetching route with ID: {}", routeId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        return routeMapper.toResponseWithCityCodes(route, cityRepository);
    }
    
    public RouteResponse updateRoute(Long routeId, RouteRequest request) {
        log.info("Updating route with ID: {}", routeId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        
        // Handle city code updates
        if (request.getSourceCityCode() != null) {
            City sourceCity = cityRepository.findByCityCode(request.getSourceCityCode().toUpperCase())
                    .orElseThrow(() -> new IllegalArgumentException("Source city not found with code: " + request.getSourceCityCode()));
            route.setSourceCityId(sourceCity.getCityId());
        }
        
        if (request.getDestinationCityCode() != null) {
            City destCity = cityRepository.findByCityCode(request.getDestinationCityCode().toUpperCase())
                    .orElseThrow(() -> new IllegalArgumentException("Destination city not found with code: " + request.getDestinationCityCode()));
            route.setDestinationCityId(destCity.getCityId());
        }
        
        if (request.getSourceCityCode() != null && request.getDestinationCityCode() != null) {
            City sourceCity = cityRepository.findByCityCode(request.getSourceCityCode().toUpperCase()).orElse(null);
            City destCity = cityRepository.findByCityCode(request.getDestinationCityCode().toUpperCase()).orElse(null);
            if (sourceCity != null && destCity != null && sourceCity.getCityId().equals(destCity.getCityId())) {
                throw new IllegalArgumentException("Source and destination cities cannot be the same");
            }
        }
        
        // Update other fields
        if (request.getDistanceKm() != null) route.setDistanceKm(request.getDistanceKm());
        if (request.getEstimatedDurationMinutes() != null) route.setEstimatedDurationMinutes(request.getEstimatedDurationMinutes());
        if (request.getIsActive() != null) route.setIsActive(request.getIsActive());
        
        Route updatedRoute = routeRepository.save(route);
        log.info("Route updated successfully with ID: {}", updatedRoute.getRouteId());
        return routeMapper.toResponseWithCityCodes(updatedRoute, cityRepository);
    }
}
