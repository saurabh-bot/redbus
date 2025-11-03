package com.redbus.fleet.service;

import com.redbus.fleet.constants.SeatConstants;
import com.redbus.fleet.exception.SeatAlreadyBookedException;
import com.redbus.fleet.exception.SeatAlreadyLockedException;
import com.redbus.fleet.repository.SeatReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatLockServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private SeatReservationRepository seatReservationRepository;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private SeatLockService seatLockService;

    @Test
    void testLockSeats_Success() {
        Long tripId = 100L;
        List<String> seatNumbers = Arrays.asList("A1", "A2");
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        when(redisTemplate.keys(anyString())).thenReturn(Collections.emptySet());
        when(seatReservationRepository.countOverlappingReservations(any(), any(), any(), any()))
                .thenReturn(0L);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        boolean result = seatLockService.lockSeats(tripId, seatNumbers, fromStopSequence, toStopSequence);

        assertTrue(result);
        verify(redisTemplate.opsForValue(), times(2)).set(
                anyString(),
                eq(SeatConstants.LOCK_VALUE),
                eq(Duration.ofMinutes(SeatConstants.LOCK_TTL_MINUTES))
        );
        verify(seatReservationRepository, times(2))
                .countOverlappingReservations(eq(tripId), anyString(), eq(fromStopSequence), eq(toStopSequence));
    }

    @Test
    void testLockSeats_SeatAlreadyLocked() {
        Long tripId = 100L;
        List<String> seatNumbers = Collections.singletonList("A1");
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        String lockKeyPattern = SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER +
                "A1" + SeatConstants.LOCK_KEY_DELIMITER + "*";
        Set<String> existingKeys = Set.of(
                SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER +
                        "A1" + SeatConstants.LOCK_KEY_DELIMITER + "1" + SeatConstants.LOCK_KEY_DELIMITER + "3"
        );

        when(redisTemplate.keys(eq(lockKeyPattern))).thenReturn(existingKeys);

        SeatAlreadyLockedException exception = assertThrows(
                SeatAlreadyLockedException.class,
                () -> seatLockService.lockSeats(tripId, seatNumbers, fromStopSequence, toStopSequence)
        );

        assertTrue(exception.getMessage().contains("A1"));
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void testLockSeats_SeatAlreadyBooked() {
        Long tripId = 100L;
        List<String> seatNumbers = Collections.singletonList("A1");
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        String lockKeyPattern = SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER +
                "A1" + SeatConstants.LOCK_KEY_DELIMITER + "*";
        when(redisTemplate.keys(eq(lockKeyPattern))).thenReturn(Collections.emptySet());
        when(seatReservationRepository.countOverlappingReservations(any(), any(), any(), any()))
                .thenReturn(1L);

        SeatAlreadyBookedException exception = assertThrows(
                SeatAlreadyBookedException.class,
                () -> seatLockService.lockSeats(tripId, seatNumbers, fromStopSequence, toStopSequence)
        );

        assertTrue(exception.getMessage().contains("A1"));
        verify(redisTemplate, never()).opsForValue();
    }

    @Test
    void testReleaseSeats_Success() {
        Long tripId = 100L;
        List<String> seatNumbers = Arrays.asList("A1", "A2");
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        when(redisTemplate.hasKey(anyString())).thenReturn(true);

        boolean result = seatLockService.releaseSeats(tripId, seatNumbers, fromStopSequence, toStopSequence);

        assertTrue(result);
        verify(redisTemplate, times(2)).hasKey(anyString());
        verify(redisTemplate, times(2)).delete(anyString());
    }

    @Test
    void testReleaseSeats_NoLockExists() {
        Long tripId = 100L;
        List<String> seatNumbers = Collections.singletonList("A1");
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        when(redisTemplate.hasKey(anyString())).thenReturn(false);

        boolean result = seatLockService.releaseSeats(tripId, seatNumbers, fromStopSequence, toStopSequence);

        assertTrue(result);
        verify(redisTemplate).hasKey(anyString());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void testIsSeatLockedForOverlappingSegment_Overlapping() {
        Long tripId = 100L;
        String seatNumber = "A1";
        Integer fromStopSequence = 2;
        Integer toStopSequence = 3;

        Set<String> existingKeys = Set.of(
                SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER +
                        seatNumber + SeatConstants.LOCK_KEY_DELIMITER + "1" + SeatConstants.LOCK_KEY_DELIMITER + "4"
        );

        when(redisTemplate.keys(anyString())).thenReturn(existingKeys);

        boolean result = seatLockService.isSeatLockedForOverlappingSegment(
                tripId, seatNumber, fromStopSequence, toStopSequence);

        assertTrue(result);
    }

    @Test
    void testIsSeatLockedForOverlappingSegment_NoOverlap() {
        Long tripId = 100L;
        String seatNumber = "A1";
        Integer fromStopSequence = 4;
        Integer toStopSequence = 5;

        Set<String> existingKeys = Set.of(
                SeatConstants.LOCK_KEY_PREFIX + tripId + SeatConstants.LOCK_KEY_DELIMITER +
                        seatNumber + SeatConstants.LOCK_KEY_DELIMITER + "1" + SeatConstants.LOCK_KEY_DELIMITER + "4"
        );

        when(redisTemplate.keys(anyString())).thenReturn(existingKeys);

        boolean result = seatLockService.isSeatLockedForOverlappingSegment(
                tripId, seatNumber, fromStopSequence, toStopSequence);

        assertFalse(result);
    }

    @Test
    void testIsSeatLockedForOverlappingSegment_NoLocks() {
        Long tripId = 100L;
        String seatNumber = "A1";
        Integer fromStopSequence = 1;
        Integer toStopSequence = 3;

        when(redisTemplate.keys(anyString())).thenReturn(Collections.emptySet());

        boolean result = seatLockService.isSeatLockedForOverlappingSegment(
                tripId, seatNumber, fromStopSequence, toStopSequence);

        assertFalse(result);
    }
}

