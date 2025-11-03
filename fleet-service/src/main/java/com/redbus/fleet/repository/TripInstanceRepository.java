package com.redbus.fleet.repository;

import com.redbus.fleet.model.TripInstance;
import com.redbus.fleet.model.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripInstanceRepository extends JpaRepository<TripInstance, Long> {
    List<TripInstance> findByRouteId(Long routeId);
    
    Optional<TripInstance> findByRouteIdAndDate(Long routeId, LocalDate date);
    
    List<TripInstance> findByRouteIdAndDateBetween(Long routeId, LocalDate startDate, LocalDate endDate);
    
    List<TripInstance> findByDateAndStatus(LocalDate date, TripStatus status);
}
