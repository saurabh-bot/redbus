package com.redbus.fleet.service;

import com.redbus.fleet.constants.SeatConstants;
import com.redbus.fleet.exception.SeatAlreadyBookedException;
import com.redbus.fleet.exception.SeatAlreadyLockedException;
import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.SeatLayout;
import com.redbus.fleet.repository.SeatReservationRepository;
import com.redbus.fleet.repository.SeatLayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatLockService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final SeatReservationRepository seatReservationRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    
    public boolean lockSeats(Long tripId, List<String> seatNumbers, Integer fromStopSequence, 
                            Integer toStopSequence) {
        for (String seatNumber : seatNumbers) {
            if (isSeatLockedForOverlappingSegment(tripId, seatNumber, fromStopSequence, toStopSequence)) {
                log.error("Seat {} is already locked for overlapping route segment (trip: {}, {}->{})", 
                        seatNumber, tripId, fromStopSequence, toStopSequence);
                throw new SeatAlreadyLockedException(String.format(
                        SeatConstants.ERROR_SEAT_ALREADY_LOCKED, seatNumber, tripId, fromStopSequence, toStopSequence));
            }
            
            long overlappingCount = seatReservationRepository.countOverlappingReservations(
                    tripId, seatNumber, fromStopSequence, toStopSequence);
            
            if (overlappingCount > 0) {
                log.error("Seat {} has overlapping confirmed booking (trip: {}, {}->{})", 
                        seatNumber, tripId, fromStopSequence, toStopSequence);
                throw new SeatAlreadyBookedException(String.format(
                        SeatConstants.ERROR_SEAT_ALREADY_BOOKED, seatNumber, tripId, fromStopSequence, toStopSequence));
            }
            
            String lockKey = buildLockKey(tripId, seatNumber, fromStopSequence, toStopSequence);
            redisTemplate.opsForValue().set(lockKey, SeatConstants.LOCK_VALUE, Duration.ofMinutes(SeatConstants.LOCK_TTL_MINUTES));
        }
        
        log.info("Seats locked successfully. Trip: {}, Seats: {}, Segments: {}->{}", 
                tripId, seatNumbers, fromStopSequence, toStopSequence);
        return true;
    }
    
    /**
     * Release seats by exact match of trip, seat numbers, and sequences.
     * Booking Service must call this with specific seat details.
     */
    public boolean releaseSeats(Long tripId, List<String> seatNumbers, Integer fromStopSequence, Integer toStopSequence) {
        int releasedCount = 0;
        for (String seatNumber : seatNumbers) {
            String lockKey = buildLockKey(tripId, seatNumber, fromStopSequence, toStopSequence);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(lockKey))) {
                redisTemplate.delete(lockKey);
                releasedCount++;
            }
        }
        
        log.info("Seats released successfully. Trip: {}, Seats: {}, Released: {}", tripId, seatNumbers, releasedCount);
        return true;
    }
    
    /**
     * Check if a seat is locked for an overlapping route segment.
     * Two segments overlap if they share any common stops.
     * 
     * Examples:
     * - Lock 1->4 overlaps with request 1->3 (shares stops 1,2,3)
     * - Lock 1->4 overlaps with request 2->5 (shares stops 2,3,4)
     * - Lock 1->4 does NOT overlap with request 4->5 (no common stops)
     * - Lock 1->4 does NOT overlap with request 5->6 (no common stops)
     */
    public boolean isSeatLockedForOverlappingSegment(Long tripId, String seatNumber, 
                                                      Integer fromStopSequence, Integer toStopSequence) {
        String pattern = SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER + seatNumber + SeatConstants.LOCK_KEY_DELIMITER + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        
        if (keys == null || keys.isEmpty()) {
            return false;
        }
        
        for (String key : keys) {
            String[] parts = key.split(SeatConstants.LOCK_KEY_DELIMITER);
            if (parts.length >= SeatConstants.MIN_LOCK_KEY_PARTS) {
                try {
                    int lockedFrom = Integer.parseInt(parts[3]);
                    int lockedTo = Integer.parseInt(parts[4]);
                    
                    // Overlap condition for [a,b) and [c,d): a < d AND b > c
                    boolean overlaps = (lockedFrom < toStopSequence && lockedTo > fromStopSequence);
                    if (overlaps) {
                        return true;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    log.error("Invalid lock key format: {}", key, e);
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if a seat is locked for exact route segment match (for backward compatibility).
     * @deprecated Use isSeatLockedForOverlappingSegment instead
     */
    @Deprecated
    public boolean isSeatLocked(Long tripId, String seatNumber, Integer fromStopSequence, Integer toStopSequence) {
        String lockKey = buildLockKey(tripId, seatNumber, fromStopSequence, toStopSequence);
        return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
    }
    
    private String buildLockKey(Long tripId, String seatNumber, Integer fromStopSequence, Integer toStopSequence) {
        return String.format("%s%d%s%s%s%d%s%d", 
                SeatConstants.LOCK_KEY_PREFIX, tripId, SeatConstants.LOCK_KEY_DELIMITER, 
                seatNumber, SeatConstants.LOCK_KEY_DELIMITER, fromStopSequence, 
                SeatConstants.LOCK_KEY_DELIMITER, toStopSequence);
    }
}
