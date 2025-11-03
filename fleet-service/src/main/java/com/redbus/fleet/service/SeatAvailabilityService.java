package com.redbus.fleet.service;

import com.redbus.fleet.dto.SeatAvailabilityResponse;
import com.redbus.fleet.dto.SeatInfo;
import com.redbus.fleet.model.Route;
import com.redbus.fleet.model.SeatReservation;
import com.redbus.fleet.model.SeatLayout;
import com.redbus.fleet.model.TripInstance;
import com.redbus.fleet.model.enums.SeatAvailabilityStatus;
import com.redbus.fleet.model.enums.SeatReservationStatus;
import com.redbus.fleet.repository.SeatReservationRepository;
import com.redbus.fleet.repository.SeatLayoutRepository;
import com.redbus.fleet.repository.TripInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SeatAvailabilityService {
    
    private final SeatLayoutRepository seatLayoutRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final SeatLockService seatLockService;
    private final TripInstanceRepository tripInstanceRepository;
    
    public SeatAvailabilityResponse getSeatAvailability(Long tripId, Integer fromStopSequence, Integer toStopSequence) {
        TripInstance trip = tripInstanceRepository.findById(tripId)
                .orElseThrow(() -> {
                    log.error("Trip not found with ID: {}", tripId);
                    return new IllegalArgumentException("Trip not found with ID: " + tripId);
                });
        
        Route route = trip.getRoute();
        if (route == null) {
            log.error("Route not found for trip ID: {}", tripId);
            throw new IllegalArgumentException("Route not found for trip ID: " + tripId);
        }
        
        Long busId = route.getBusId();
        if (busId == null) {
            log.error("Bus ID not found for route ID: {} (trip ID: {})", route.getRouteId(), tripId);
            throw new IllegalArgumentException("Bus ID not found for route ID: " + route.getRouteId() + " (trip ID: " + tripId + ")");
        }
        
        List<SeatLayout> allSeats = seatLayoutRepository.findByBusId(busId);
        if (allSeats.isEmpty()) {
            log.warn("No seat layouts found for bus ID: {} (trip ID: {})", busId, tripId);
        }
        
        List<SeatReservation> reservations = seatReservationRepository.findByTripIdAndStatus(tripId, SeatReservationStatus.CREATED);
        
        // Create seat map with availability
        List<SeatInfo> seatMap = allSeats.stream()
                .map(seat -> {
                    // Check if seat is booked for overlapping route segment
                    // If record exists in seat_reservations = BOOKED, no record = AVAILABLE
                    // LOCKED state exists only in Redis (temporary)
                    boolean isBooked = isSeatBooked(seat.getSeatNumber(), reservations, fromStopSequence, toStopSequence);
                    
                    // Check if seat is locked (in Redis) for overlapping route segment
                    boolean isLocked = seatLockService.isSeatLockedForOverlappingSegment(
                            tripId, seat.getSeatNumber(), fromStopSequence, toStopSequence);
                    
                    // Three states: AVAILABLE, LOCKED, or BOOKED
                    // Priority: BOOKED > LOCKED > AVAILABLE
                    SeatAvailabilityStatus availabilityStatus;
                    if (isBooked) {
                        availabilityStatus = SeatAvailabilityStatus.BOOKED;
                    } else if (isLocked) {
                        availabilityStatus = SeatAvailabilityStatus.LOCKED;
                    } else {
                        availabilityStatus = SeatAvailabilityStatus.AVAILABLE;
                    }
                    
                    return SeatInfo.builder()
                            .seatNumber(seat.getSeatNumber())
                            .seatType(seat.getSeatType())
                            .deck(seat.getDeck())
                            .availabilityStatus(availabilityStatus)
                            .price(seat.getPrice())
                            .isLadiesSeat(seat.getIsLadiesSeat())
                            .positionX(seat.getPositionX())
                            .positionY(seat.getPositionY())
                            .rowNumber(seat.getRowNumber())
                            .columnNumber(seat.getColumnNumber())
                            .build();
                })
                .collect(Collectors.toList());
        
        long availableCount = seatMap.stream()
                .filter(seat -> seat.getAvailabilityStatus() == SeatAvailabilityStatus.AVAILABLE)
                .count();
        
        return SeatAvailabilityResponse.builder()
                .tripId(tripId)
                .fromStopSequence(fromStopSequence)
                .toStopSequence(toStopSequence)
                .availableSeats((int) availableCount)
                .totalSeats(allSeats.size())
                .seatMap(seatMap)
                .build();
    }
    
    private boolean isSeatBooked(String seatNumber, List<SeatReservation> reservations, 
                                Integer fromStopSequence, Integer toStopSequence) {
        return reservations.stream()
                .filter(reservation -> reservation.getSeatNumber().equals(seatNumber))
                .anyMatch(reservation -> {
                    // Check if reservations overlap
                    return !(reservation.getBookedToStopSequence() <= fromStopSequence ||
                            reservation.getBookedFromStopSequence() >= toStopSequence);
                });
    }
}
