package com.redbus.fleet.service;

import com.redbus.fleet.constants.SeatConstants;
import com.redbus.fleet.exception.SeatLockNotFoundException;
import com.redbus.fleet.exception.SeatAlreadyBookedException;
import com.redbus.fleet.exception.SeatReservationNotFoundException;
import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.enums.SeatReservationStatus;
import com.redbus.fleet.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatReservationService {
    
    private final SeatReservationRepository seatReservationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * Confirm seats by exact match of trip, seat numbers, and sequences.
     * Steps:
     * 1. Check if lock exists in Redis - throw exception if not found
     * 2. Check if seat is available in DB (no overlapping CONFIRMED reservation)
     * 3. Create CONFIRMED reservation in database
     * 4. Delete lock from Redis
     */
    public void confirmSeats(Long tripId, List<String> seatNumbers, Integer fromStopSequence, Integer toStopSequence) {
        for (String seatNumber : seatNumbers) {
            String lockKey = buildLockKey(tripId, seatNumber, fromStopSequence, toStopSequence);
            
            if (!Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
                log.error("No lock found for seat {} on trip {} with sequences {}->{}. Cannot confirm.", 
                        seatNumber, tripId, fromStopSequence, toStopSequence);
                throw new SeatLockNotFoundException(String.format(SeatConstants.ERROR_NO_LOCK_FOUND, 
                        seatNumber, tripId, fromStopSequence, toStopSequence));
            }
            
            long overlappingCount = seatReservationRepository.countOverlappingReservations(
                    tripId, seatNumber, fromStopSequence, toStopSequence);
            
            if (overlappingCount > 0) {
                log.error("Seat {} is already booked for trip {} with overlapping sequences {}->{}", 
                        seatNumber, tripId, fromStopSequence, toStopSequence);
                throw new SeatAlreadyBookedException(String.format(SeatConstants.ERROR_SEAT_ALREADY_BOOKED, 
                        seatNumber, tripId, fromStopSequence, toStopSequence));
            }
            
            SeatReservation seatReservation = SeatReservation.builder()
                    .tripId(tripId)
                    .seatNumber(seatNumber)
                    .bookedFromStopSequence(fromStopSequence)
                    .bookedToStopSequence(toStopSequence)
                    .status(SeatReservationStatus.CREATED)
                    .build();
            seatReservationRepository.save(seatReservation);
            
            redisTemplate.delete(lockKey);
        }
        
        log.info("Seats confirmed successfully. Trip: {}, Seats: {}, Segments: {}->{}", 
                tripId, seatNumbers, fromStopSequence, toStopSequence);
    }
    
    /**
     * Cancel seats by updating their status to CANCELLED.
     * Can be cancelled regardless of current status.
     */
    public void cancelSeats(Long tripId, List<String> seatNumbers, Integer fromStopSequence, Integer toStopSequence) {
        for (String seatNumber : seatNumbers) {
            List<SeatReservation> reservations = seatReservationRepository.findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                    tripId, seatNumber, fromStopSequence, toStopSequence);
            
            if (reservations.isEmpty()) {
                log.error("No reservation found for seat {} on trip {} with sequences {}->{}", 
                        seatNumber, tripId, fromStopSequence, toStopSequence);
                throw new SeatReservationNotFoundException(String.format(SeatConstants.ERROR_NO_RESERVATION_FOUND, 
                        seatNumber, tripId, fromStopSequence, toStopSequence));
            }
            
            for (SeatReservation reservation : reservations) {
                reservation.setStatus(SeatReservationStatus.CANCELLED);
                seatReservationRepository.save(reservation);
            }
        }
        
        log.info("Seats cancelled successfully. Trip: {}, Seats: {}, Segments: {}->{}", 
                tripId, seatNumbers, fromStopSequence, toStopSequence);
    }
    
    private String buildLockKey(Long tripId, String seatNumber, Integer fromStopSequence, Integer toStopSequence) {
        return String.format("%s%d%s%s%s%d%s%d", 
                SeatConstants.LOCK_KEY_PREFIX, tripId, SeatConstants.LOCK_KEY_DELIMITER, 
                seatNumber, SeatConstants.LOCK_KEY_DELIMITER, fromStopSequence, 
                SeatConstants.LOCK_KEY_DELIMITER, toStopSequence);
    }
    
}
