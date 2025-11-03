package com.redbus.fleet.service;

import com.redbus.fleet.model.Route;
import com.redbus.fleet.model.TripInstance;
import com.redbus.fleet.model.enums.TripStatus;
import com.redbus.fleet.repository.RouteRepository;
import com.redbus.fleet.repository.TripInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TripInstanceService {
    
    private final TripInstanceRepository tripInstanceRepository;
    private final RouteRepository routeRepository;
    
    public void generateTripInstancesForNext180Days() {
        log.info("Starting trip instance generation for next 180 days");
        
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(180);
        
        // Get all active routes
        List<Route> activeRoutes = routeRepository.findByIsActive(true);
        log.info("Found {} active routes", activeRoutes.size());
        
        java.util.concurrent.atomic.AtomicInteger createdCount = new java.util.concurrent.atomic.AtomicInteger(0);
        
        for (Route route : activeRoutes) {
            LocalDate currentDate = today;
            
            while (!currentDate.isAfter(targetDate)) {
                final LocalDate dateToCheck = currentDate;
                // Check if trip instance already exists
                tripInstanceRepository.findByRouteIdAndDate(route.getRouteId(), dateToCheck)
                        .ifPresentOrElse(
                                existing -> {
                                    // Trip already exists, skip
                                },
                                () -> {
                                    // Create new trip instance
                                    TripInstance trip = TripInstance.builder()
                                            .routeId(route.getRouteId())
                                            .date(dateToCheck)
                                            .status(TripStatus.SCHEDULED)
                                            .build();
                                    
                                    tripInstanceRepository.save(trip);
                                    createdCount.incrementAndGet();
                                }
                        );
                
                currentDate = currentDate.plusDays(1);
            }
        }
        
        log.info("Trip instance generation completed. Created {} new trip instances", createdCount.get());
    }
    
    public TripInstance createTripInstance(Long routeId, LocalDate date) {
        log.info("Creating trip instance for route ID: {} on date: {}", routeId, date);
        
        // Verify route exists
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new IllegalArgumentException("Route not found with ID: " + routeId));
        
        // Check if already exists
        tripInstanceRepository.findByRouteIdAndDate(routeId, date)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Trip instance already exists for route " + routeId + " on " + date);
                });
        
        TripInstance trip = TripInstance.builder()
                .routeId(routeId)
                .date(date)
                .status(TripStatus.SCHEDULED)
                .build();
        
        TripInstance savedTrip = tripInstanceRepository.save(trip);
        log.info("Trip instance created with ID: {}", savedTrip.getTripId());
        return savedTrip;
    }
    
    @Transactional(readOnly = true)
    public List<TripInstance> getTripsByRoute(Long routeId) {
        return tripInstanceRepository.findByRouteId(routeId);
    }
    
    @Transactional(readOnly = true)
    public TripInstance getTripById(Long tripId) {
        return tripInstanceRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip instance not found with ID: " + tripId));
    }
    
}
