package com.redbus.fleet.service;

import com.redbus.fleet.constants.SeatConstants;
import com.redbus.fleet.exception.SeatAlreadyBookedException;
import com.redbus.fleet.exception.SeatLockNotFoundException;
import com.redbus.fleet.exception.SeatReservationNotFoundException;
import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.enums.SeatReservationStatus;
import com.redbus.fleet.repository.SeatReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatReservationServiceTest {

    @Mock
    private SeatReservationRepository seatReservationRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private SeatReservationService seatReservationService;

    private Long tripId;
    private List<String> seatNumbers;
    private Integer fromStopSequence;
    private Integer toStopSequence;

    @BeforeEach
    void setUp() {
        tripId = 100L;
        seatNumbers = Arrays.asList("A1", "A2");
        fromStopSequence = 1;
        toStopSequence = 3;
    }

    @Test
    void testConfirmSeats_Success() {
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(seatReservationRepository.countOverlappingReservations(any(), any(), any(), any()))
                .thenReturn(0L);
        when(seatReservationRepository.save(any(SeatReservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        seatReservationService.confirmSeats(tripId, seatNumbers, fromStopSequence, toStopSequence);

        verify(redisTemplate, times(2)).hasKey(anyString());
        verify(seatReservationRepository, times(2))
                .countOverlappingReservations(eq(tripId), anyString(), eq(fromStopSequence), eq(toStopSequence));
        verify(seatReservationRepository, times(2)).save(any(SeatReservation.class));
        verify(redisTemplate, times(2)).delete(anyString());

        ArgumentCaptor<SeatReservation> reservationCaptor = ArgumentCaptor.forClass(SeatReservation.class);
        verify(seatReservationRepository, times(2)).save(reservationCaptor.capture());

        List<SeatReservation> savedReservations = reservationCaptor.getAllValues();
        assertEquals(2, savedReservations.size());
        savedReservations.forEach(reservation -> {
            assertEquals(tripId, reservation.getTripId());
            assertEquals(fromStopSequence, reservation.getBookedFromStopSequence());
            assertEquals(toStopSequence, reservation.getBookedToStopSequence());
            assertEquals(SeatReservationStatus.CREATED, reservation.getStatus());
        });
    }

    @Test
    void testConfirmSeats_LockNotFound() {
        when(redisTemplate.hasKey(anyString())).thenReturn(false);

        SeatLockNotFoundException exception = assertThrows(
                SeatLockNotFoundException.class,
                () -> seatReservationService.confirmSeats(tripId, seatNumbers, fromStopSequence, toStopSequence)
        );

        assertTrue(exception.getMessage().contains("A1"));
        verify(seatReservationRepository, never()).save(any());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void testConfirmSeats_SeatAlreadyBooked() {
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(seatReservationRepository.countOverlappingReservations(any(), any(), any(), any()))
                .thenReturn(1L);

        SeatAlreadyBookedException exception = assertThrows(
                SeatAlreadyBookedException.class,
                () -> seatReservationService.confirmSeats(tripId, seatNumbers, fromStopSequence, toStopSequence)
        );

        assertTrue(exception.getMessage().contains("A1"));
        verify(seatReservationRepository, never()).save(any());
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void testCancelSeats_Success() {
        SeatReservation reservation1 = SeatReservation.builder()
                .tripId(tripId)
                .seatNumber("A1")
                .bookedFromStopSequence(fromStopSequence)
                .bookedToStopSequence(toStopSequence)
                .status(SeatReservationStatus.CREATED)
                .build();

        SeatReservation reservation2 = SeatReservation.builder()
                .tripId(tripId)
                .seatNumber("A2")
                .bookedFromStopSequence(fromStopSequence)
                .bookedToStopSequence(toStopSequence)
                .status(SeatReservationStatus.CREATED)
                .build();

        when(seatReservationRepository.findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                eq(tripId), eq("A1"), eq(fromStopSequence), eq(toStopSequence)))
                .thenReturn(Collections.singletonList(reservation1));
        when(seatReservationRepository.findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                eq(tripId), eq("A2"), eq(fromStopSequence), eq(toStopSequence)))
                .thenReturn(Collections.singletonList(reservation2));
        when(seatReservationRepository.save(any(SeatReservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        seatReservationService.cancelSeats(tripId, seatNumbers, fromStopSequence, toStopSequence);

        verify(seatReservationRepository, times(2))
                .findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                        eq(tripId), anyString(), eq(fromStopSequence), eq(toStopSequence));
        verify(seatReservationRepository, times(2)).save(any(SeatReservation.class));

        ArgumentCaptor<SeatReservation> reservationCaptor = ArgumentCaptor.forClass(SeatReservation.class);
        verify(seatReservationRepository, times(2)).save(reservationCaptor.capture());

        List<SeatReservation> savedReservations = reservationCaptor.getAllValues();
        savedReservations.forEach(reservation ->
                assertEquals(SeatReservationStatus.CANCELLED, reservation.getStatus())
        );
    }

    @Test
    void testCancelSeats_ReservationNotFound() {
        when(seatReservationRepository.findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        SeatReservationNotFoundException exception = assertThrows(
                SeatReservationNotFoundException.class,
                () -> seatReservationService.cancelSeats(tripId, seatNumbers, fromStopSequence, toStopSequence)
        );

        assertTrue(exception.getMessage().contains("A1"));
        verify(seatReservationRepository, never()).save(any());
    }

    @Test
    void testCancelSeats_MultipleReservationsForSameSeat() {
        SeatReservation reservation1 = SeatReservation.builder()
                .tripId(tripId)
                .seatNumber("A1")
                .bookedFromStopSequence(fromStopSequence)
                .bookedToStopSequence(toStopSequence)
                .status(SeatReservationStatus.CREATED)
                .build();

        SeatReservation reservation2 = SeatReservation.builder()
                .tripId(tripId)
                .seatNumber("A1")
                .bookedFromStopSequence(fromStopSequence)
                .bookedToStopSequence(toStopSequence)
                .status(SeatReservationStatus.CANCELLED)
                .build();

        when(seatReservationRepository.findByTripIdAndSeatNumberAndBookedFromStopSequenceAndBookedToStopSequence(
                eq(tripId), eq("A1"), eq(fromStopSequence), eq(toStopSequence)))
                .thenReturn(Arrays.asList(reservation1, reservation2));
        when(seatReservationRepository.save(any(SeatReservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        seatReservationService.cancelSeats(tripId, Collections.singletonList("A1"), fromStopSequence, toStopSequence);

        verify(seatReservationRepository, times(2)).save(any(SeatReservation.class));
        
        ArgumentCaptor<SeatReservation> reservationCaptor = ArgumentCaptor.forClass(SeatReservation.class);
        verify(seatReservationRepository, times(2)).save(reservationCaptor.capture());

        List<SeatReservation> savedReservations = reservationCaptor.getAllValues();
        savedReservations.forEach(reservation ->
                assertEquals(SeatReservationStatus.CANCELLED, reservation.getStatus())
        );
    }
}

