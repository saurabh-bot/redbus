package com.redbus.fleet.repository;

import com.redbus.fleet.model.Bus;
import com.redbus.fleet.model.enums.BusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    Optional<Bus> findByBusNumber(String busNumber);
    
    Page<Bus> findByOperatorId(Long operatorId, Pageable pageable);
    
    Page<Bus> findByIsActive(Boolean isActive, Pageable pageable);
    
    Page<Bus> findBySeatType(BusType seatType, Pageable pageable);
    
    Page<Bus> findByOperatorIdAndIsActive(Long operatorId, Boolean isActive, Pageable pageable);
}
