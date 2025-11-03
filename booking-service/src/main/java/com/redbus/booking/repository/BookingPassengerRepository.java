package com.redbus.booking.repository;

import com.redbus.booking.model.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {
    List<BookingPassenger> findByBookingId(Long bookingId);
    
    Optional<BookingPassenger> findByBookingIdAndSeatNumber(Long bookingId, String seatNumber);
    
    List<BookingPassenger> findByBookingIdAndSeatNumberIn(Long bookingId, List<String> seatNumbers);
    
    void deleteByBookingId(Long bookingId);
}
