package com.redbus.fleet.repository;

import com.redbus.fleet.model.SeatLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatLayoutRepository extends JpaRepository<SeatLayout, Long> {
    List<SeatLayout> findByBusId(Long busId);
    
    Optional<SeatLayout> findByBusIdAndSeatNumber(Long busId, String seatNumber);
    
    long countByBusId(Long busId);
}
