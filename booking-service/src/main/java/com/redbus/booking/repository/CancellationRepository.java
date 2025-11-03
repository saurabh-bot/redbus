package com.redbus.booking.repository;

import com.redbus.booking.model.Cancellation;
import com.redbus.booking.model.enums.CancellationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
    Optional<Cancellation> findByBookingId(Long bookingId);
    
    List<Cancellation> findByUserId(Long userId);
    
    List<Cancellation> findByStatus(CancellationStatus status);
    
    List<Cancellation> findByBookingIdAndStatus(Long bookingId, CancellationStatus status);
}
