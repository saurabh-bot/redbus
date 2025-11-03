package com.redbus.fleet.service;

import com.redbus.fleet.dto.SeatLayoutListResponse;
import com.redbus.fleet.dto.SeatLayoutRequest;
import com.redbus.fleet.dto.SeatLayoutResponse;
import com.redbus.fleet.mapper.SeatLayoutMapper;
import com.redbus.fleet.model.Bus;
import com.redbus.fleet.model.SeatLayout;
import com.redbus.fleet.repository.BusRepository;
import com.redbus.fleet.repository.SeatLayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SeatLayoutService {
    
    private final SeatLayoutRepository seatLayoutRepository;
    private final BusRepository busRepository;
    private final SeatLayoutMapper seatLayoutMapper;
    
    public SeatLayoutListResponse createSeatLayout(Long busId, List<SeatLayoutRequest> requests) {
        log.info("Creating seat layout for bus ID: {} with {} seats", busId, requests.size());
        
        // Verify bus exists
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        
        // Check for duplicate seat numbers
        long uniqueSeatCount = requests.stream()
                .map(SeatLayoutRequest::getSeatNumber)
                .distinct()
                .count();
        if (uniqueSeatCount != requests.size()) {
            throw new IllegalArgumentException("Duplicate seat numbers found in the request");
        }
        
        // Check if any seat already exists
        for (SeatLayoutRequest request : requests) {
            seatLayoutRepository.findByBusIdAndSeatNumber(busId, request.getSeatNumber())
                    .ifPresent(existing -> {
                        throw new IllegalArgumentException("Seat " + request.getSeatNumber() + " already exists for bus " + busId);
                    });
        }
        
        List<SeatLayout> seatLayouts = requests.stream()
                .map(seatLayoutMapper::toEntityWithDefaults)
                .map(seat -> {
                    seat.setBusId(busId);
                    return seat;
                })
                .collect(Collectors.toList());
        
        List<SeatLayout> savedSeats = seatLayoutRepository.saveAll(seatLayouts);
        
        // Update bus total_seats
        bus.setTotalSeats(savedSeats.size());
        busRepository.save(bus);
        
        log.info("Seat layout created successfully with {} seats", savedSeats.size());
        
        return SeatLayoutListResponse.builder()
                .busId(busId)
                .total_count(savedSeats.size())
                .seats(savedSeats.stream()
                        .map(seatLayoutMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    
    @Transactional(readOnly = true)
    public SeatLayoutListResponse getSeatLayout(Long busId) {
        log.info("Fetching seat layout for bus ID: {}", busId);
        
        // Verify bus exists
        busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        
        List<SeatLayout> seats = seatLayoutRepository.findByBusId(busId);
        
        return SeatLayoutListResponse.builder()
                .busId(busId)
                .total_count(seats.size())
                .seats(seats.stream()
                        .map(seatLayoutMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
    
    public SeatLayoutResponse updateSeatLayout(Long busId, Long seatLayoutId, SeatLayoutRequest request) {
        log.info("Updating seat layout ID: {} for bus ID: {}", seatLayoutId, busId);
        
        SeatLayout seatLayout = seatLayoutRepository.findById(seatLayoutId)
                .orElseThrow(() -> new IllegalArgumentException("Seat layout not found with ID: " + seatLayoutId));
        
        if (!seatLayout.getBusId().equals(busId)) {
            throw new IllegalArgumentException("Seat layout does not belong to bus " + busId);
        }
        
        // Check seat number conflict if being changed
        if (request.getSeatNumber() != null && !request.getSeatNumber().equals(seatLayout.getSeatNumber())) {
            seatLayoutRepository.findByBusIdAndSeatNumber(busId, request.getSeatNumber())
                    .ifPresent(existing -> {
                        if (!existing.getSeatLayoutId().equals(seatLayoutId)) {
                            throw new IllegalArgumentException("Seat " + request.getSeatNumber() + " already exists for bus " + busId);
                        }
                    });
        }
        
        seatLayoutMapper.updateEntityFromRequest(request, seatLayout);
        
        // Handle defaults for update
        if (request.getIsLadiesSeat() != null) {
            seatLayout.setIsLadiesSeat(request.getIsLadiesSeat());
        }
        if (request.getBasePriceMultiplier() != null) {
            seatLayout.setBasePriceMultiplier(request.getBasePriceMultiplier());
        }
        
        SeatLayout updatedSeat = seatLayoutRepository.save(seatLayout);
        log.info("Seat layout updated successfully");
        return seatLayoutMapper.toResponse(updatedSeat);
    }
    
    public void deleteAllSeatsForBus(Long busId) {
        log.info("Deleting all seat layouts for bus ID: {}", busId);
        
        // Verify bus exists
        busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        
        List<SeatLayout> seats = seatLayoutRepository.findByBusId(busId);
        seatLayoutRepository.deleteAll(seats);
        
        // Update bus total_seats
        Bus bus = busRepository.findById(busId).orElse(null);
        if (bus != null) {
            bus.setTotalSeats(0);
            busRepository.save(bus);
        }
        
        log.info("Deleted {} seat layouts for bus ID: {}", seats.size(), busId);
    }
}
