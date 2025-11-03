package com.redbus.fleet.repository;

import com.redbus.fleet.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByBusId(Long busId);
    
    List<Route> findBySourceCityIdAndDestinationCityId(Long sourceCityId, Long destinationCityId);
    
    List<Route> findByIsActive(Boolean isActive);
}
