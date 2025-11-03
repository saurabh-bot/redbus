package com.redbus.fleet.repository;

import com.redbus.fleet.model.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    List<RouteStop> findByRouteIdOrderBySequenceAsc(Long routeId);
    
    Optional<RouteStop> findByRouteIdAndSequence(Long routeId, Integer sequence);
    
    Optional<RouteStop> findByRouteIdAndCityId(Long routeId, Long cityId);
}
