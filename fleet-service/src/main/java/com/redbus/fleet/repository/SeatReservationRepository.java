package com.redbus.fleet.repository;

import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.enums.SeatReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    List<SeatReservation> findByTripId(Long tripId);
    
    List<SeatReservation> findByTripIdAndStatus(Long tripId, SeatReservationStatus status);
    
    List<SeatReservation> findByTripIdAndSeatNumber(Long tripId, String seatNumber);
    
    List<SeatReservation> findByTripIdAndSeatNumberAndStatus(Long tripId, String seatNumber, SeatReservationStatus status);
    
    /**
     * Count overlapping reservations for a seat.
     * LOCKED reservations are checked separately in Redis (temporary locks).
     * If a record exists in seat_reservations table, it means the seat is BOOKED.
     * Two segments overlap if they share any common stops.
     * 
     * Examples:
     * - Reservation 1->4 overlaps with request 1->3 (shares stops 1,2,3)
     * - Reservation 1->4 overlaps with request 2->5 (shares stops 2,3,4)
     * - Reservation 1->4 does NOT overlap with request 4->5 (no common stops)
     */
@Query("SELECT COUNT(sr) FROM SeatReservation sr " +
           "WHERE sr.tripId = :tripId " +
           "AND sr.seatNumber = :seatNumber " +
           "AND sr.status = 'CREATED' " +
           "AND NOT (sr.bookedToStopSequence <= :fromStopSequence OR sr.bookedFromStopSequence >= :toStopSequence)")
    long countOverlappingReservations(@Param("tripId") Long tripId,
                                      @Param("seatNumber") String seatNumber,
                                      @Param("fromStopSequence") Integer fromStopSequence,
                                      @Param("toStopSequence") Integer toStopSequence);
    
    /**
     * Find seat reservations by exact match of trip, seat, and sequences
     */
    List<SeatReservation> findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
            Long tripId, String seatNumber, Integer fromStopSequence, Integer toStopSequence);
}
