package com.redbus.fleet.service;

import com.redbus.fleet.dto.SeatAvailabilityResponse;
import com.redbus.fleet.dto.SeatInfo;
import com.redbus.fleet.model.Route;
import com.redbus.fleet.model.SeatLayout;
import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.TripInstance;
import com.redbus.fleet.model.enums.Deck;
import com.redbus.fleet.model.enums.SeatAvailabilityStatus;
import com.redbus.fleet.model.enums.SeatReservationStatus;
import com.redbus.fleet.model.enums.SeatType;
import com.redbus.fleet.repository.SeatLayoutRepository;
import com.redbus.fleet.repository.SeatReservationRepository;
import com.redbus.fleet.repository.TripInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatAvailabilityServiceTest {

    @Mock
    private SeatLayoutRepository seatLayoutRepository;

    @Mock
    private SeatReservationRepository seatReservationRepository;

    @Mock
    private SeatLockService seatLockService;

    @Mock
    private TripInstanceRepository tripInstanceRepository;

    @InjectMocks
    private SeatAvailabilityService seatAvailabilityService;

    private TripInstance tripInstance;
    private Route route;
    private List<SeatLayout> seatLayouts;

    @BeforeEach
    void setUp() {
        route = Route.builder()
                .routeId(1L)
                .busId(10L)
                .build();

        tripInstance = TripInstance.builder()
                .tripId(100L)
                .route(route)
                .build();

        seatLayouts = Arrays.asList(
                SeatLayout.builder()
                        .seatLayoutId(1L)
                        .busId(10L)
                        .seatNumber("A1")
                        .seatType(SeatType.WINDOW)
                        .deck(Deck.LOWER_DECK)
                        .price(500.0)
                        .isLadiesSeat(false)
                        .positionX(10)
                        .positionY(20)
                        .rowNumber(1)
                        .columnNumber(1)
                        .build(),
                SeatLayout.builder()
                        .seatLayoutId(2L)
                        .busId(10L)
                        .seatNumber("A2")
                        .seatType(SeatType.AISLE)
                        .deck(Deck.LOWER_DECK)
                        .price(450.0)
                        .isLadiesSeat(true)
                        .positionX(50)
                        .positionY(20)
                        .rowNumber(1)
                        .columnNumber(2)
                        .build()
        );
    }

    @Test
    void testGetSeatAvailability_Success() {
        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));
        when(seatLayoutRepository.findByBusId(10L)).thenReturn(seatLayouts);
        when(seatReservationRepository.findByTripIdAndStatus(100L, SeatReservationStatus.CREATED))
                .thenReturn(Collections.emptyList());
        when(seatLockService.isSeatLockedForOverlappingSegment(any(), any(), any(), any()))
                .thenReturn(false);

        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(100L, 1, 3);

        assertNotNull(response);
        assertEquals(100L, response.getTripId());
        assertEquals(1, response.getFromStopSequence());
        assertEquals(3, response.getToStopSequence());
        assertEquals(2, response.getTotalSeats());
        assertEquals(2, response.getAvailableSeats());
        assertEquals(2, response.getSeatMap().size());

        SeatInfo seat1 = response.getSeatMap().get(0);
        assertEquals("A1", seat1.getSeatNumber());
        assertEquals(SeatAvailabilityStatus.AVAILABLE, seat1.getAvailabilityStatus());
        assertEquals(500.0, seat1.getPrice());

        verify(tripInstanceRepository).findById(100L);
        verify(seatLayoutRepository).findByBusId(10L);
        verify(seatReservationRepository).findByTripIdAndStatus(100L, SeatReservationStatus.CREATED);
    }

    @Test
    void testGetSeatAvailability_WithBookedSeat() {
        SeatReservation reservation = SeatReservation.builder()
                .tripId(100L)
                .seatNumber("A1")
                .bookedFromStopSequence(1)
                .bookedToStopSequence(3)
                .status(SeatReservationStatus.CREATED)
                .build();

        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));
        when(seatLayoutRepository.findByBusId(10L)).thenReturn(seatLayouts);
        when(seatReservationRepository.findByTripIdAndStatus(100L, SeatReservationStatus.CREATED))
                .thenReturn(Collections.singletonList(reservation));
        when(seatLockService.isSeatLockedForOverlappingSegment(any(), any(), any(), any()))
                .thenReturn(false);

        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(100L, 1, 3);

        assertEquals(1, response.getAvailableSeats());
        
        SeatInfo seat1 = response.getSeatMap().stream()
                .filter(s -> "A1".equals(s.getSeatNumber()))
                .findFirst()
                .orElse(null);
        assertNotNull(seat1);
        assertEquals(SeatAvailabilityStatus.BOOKED, seat1.getAvailabilityStatus());
    }

    @Test
    void testGetSeatAvailability_WithLockedSeat() {
        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));
        when(seatLayoutRepository.findByBusId(10L)).thenReturn(seatLayouts);
        when(seatReservationRepository.findByTripIdAndStatus(100L, SeatReservationStatus.CREATED))
                .thenReturn(Collections.emptyList());
        when(seatLockService.isSeatLockedForOverlappingSegment(eq(100L), eq("A1"), eq(1), eq(3)))
                .thenReturn(true);
        when(seatLockService.isSeatLockedForOverlappingSegment(eq(100L), eq("A2"), eq(1), eq(3)))
                .thenReturn(false);

        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(100L, 1, 3);

        SeatInfo seat1 = response.getSeatMap().stream()
                .filter(s -> "A1".equals(s.getSeatNumber()))
                .findFirst()
                .orElse(null);
        assertNotNull(seat1);
        assertEquals(SeatAvailabilityStatus.LOCKED, seat1.getAvailabilityStatus());
    }

    @Test
    void testGetSeatAvailability_TripNotFound() {
        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatAvailabilityService.getSeatAvailability(100L, 1, 3)
        );

        assertTrue(exception.getMessage().contains("Trip not found"));
        verify(seatLayoutRepository, never()).findByBusId(any());
    }

    @Test
    void testGetSeatAvailability_RouteNotFound() {
        tripInstance.setRoute(null);

        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> seatAvailabilityService.getSeatAvailability(100L, 1, 3)
        );

        assertTrue(exception.getMessage().contains("Route not found"));
    }

    @Test
    void testGetSeatAvailability_OverlappingSegmentBooking() {
        SeatReservation reservation = SeatReservation.builder()
                .tripId(100L)
                .seatNumber("A1")
                .bookedFromStopSequence(1)
                .bookedToStopSequence(4)
                .status(SeatReservationStatus.CREATED)
                .build();

        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));
        when(seatLayoutRepository.findByBusId(10L)).thenReturn(seatLayouts);
        when(seatReservationRepository.findByTripIdAndStatus(100L, SeatReservationStatus.CREATED))
                .thenReturn(Collections.singletonList(reservation));
        when(seatLockService.isSeatLockedForOverlappingSegment(any(), any(), any(), any()))
                .thenReturn(false);

        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(100L, 2, 3);

        SeatInfo seat1 = response.getSeatMap().stream()
                .filter(s -> "A1".equals(s.getSeatNumber()))
                .findFirst()
                .orElse(null);
        assertNotNull(seat1);
        assertEquals(SeatAvailabilityStatus.BOOKED, seat1.getAvailabilityStatus());
    }

    @Test
    void testGetSeatAvailability_NonOverlappingSegmentBooking() {
        SeatReservation reservation = SeatReservation.builder()
                .tripId(100L)
                .seatNumber("A1")
                .bookedFromStopSequence(1)
                .bookedToStopSequence(2)
                .status(SeatReservationStatus.CREATED)
                .build();

        when(tripInstanceRepository.findById(100L)).thenReturn(Optional.of(tripInstance));
        when(seatLayoutRepository.findByBusId(10L)).thenReturn(seatLayouts);
        when(seatReservationRepository.findByTripIdAndStatus(100L, SeatReservationStatus.CREATED))
                .thenReturn(Collections.singletonList(reservation));
        when(seatLockService.isSeatLockedForOverlappingSegment(any(), any(), any(), any()))
                .thenReturn(false);

        SeatAvailabilityResponse response = seatAvailabilityService.getSeatAvailability(100L, 3, 4);

        SeatInfo seat1 = response.getSeatMap().stream()
                .filter(s -> "A1".equals(s.getSeatNumber()))
                .findFirst()
                .orElse(null);
        assertNotNull(seat1);
        assertEquals(SeatAvailabilityStatus.AVAILABLE, seat1.getAvailabilityStatus());
    }
}

