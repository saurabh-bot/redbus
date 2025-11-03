package com.redbus.fleet.service;

import com.redbus.fleet.dto.BusListResponse;
import com.redbus.fleet.dto.BusRequest;
import com.redbus.fleet.dto.BusResponse;
import com.redbus.fleet.mapper.BusMapper;
import com.redbus.fleet.model.Bus;
import com.redbus.fleet.model.enums.BusType;
import com.redbus.fleet.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BusService {
    
    private final BusRepository busRepository;
    private final BusMapper busMapper;
    
    public BusResponse createBus(BusRequest request) {
        log.info("Creating bus with number: {}", request.getBusNumber());
        
        // Check if bus number already exists
        busRepository.findByBusNumber(request.getBusNumber())
                .ifPresent(bus -> {
                    throw new IllegalArgumentException("Bus with number " + request.getBusNumber() + " already exists");
                });
        
        Bus bus = busMapper.toEntity(request);
        
        // Convert amenities Map to JSON string
        if (request.getAmenities() != null) {
            bus.setAmenities(busMapper.amenitiesToString(request.getAmenities()));
        }
        
        Bus savedBus = busRepository.save(bus);
        log.info("Bus created successfully with ID: {}", savedBus.getBusId());
        return busMapper.toResponseWithDateTime(savedBus);
    }
    
    @Transactional(readOnly = true)
    public BusResponse getBusById(Long busId) {
        log.info("Fetching bus with ID: {}", busId);
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        return busMapper.toResponseWithDateTime(bus);
    }
    
    public BusResponse updateBus(Long busId, BusRequest request) {
        log.info("Updating bus with ID: {}", busId);
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new IllegalArgumentException("Bus not found with ID: " + busId));
        
        // Check if new bus number conflicts with existing
        if (request.getBusNumber() != null && !request.getBusNumber().equals(bus.getBusNumber())) {
            busRepository.findByBusNumber(request.getBusNumber())
                    .ifPresent(existingBus -> {
                        throw new IllegalArgumentException("Bus with number " + request.getBusNumber() + " already exists");
                    });
        }
        
        busMapper.updateEntityFromRequest(request, bus);
        
        // Update amenities if provided
        if (request.getAmenities() != null) {
            bus.setAmenities(busMapper.amenitiesToString(request.getAmenities()));
        }
        
        Bus updatedBus = busRepository.save(bus);
        log.info("Bus updated successfully with ID: {}", updatedBus.getBusId());
        return busMapper.toResponseWithDateTime(updatedBus);
    }
    
    @Transactional(readOnly = true)
    public BusListResponse listBuses(Long operatorId, Boolean isActive, BusType seatType, Integer page, Integer pageSize) {
        log.info("Listing buses with filters - operatorId: {}, isActive: {}, seatType: {}, page: {}, pageSize: {}",
                operatorId, isActive, seatType, page, pageSize);
        
        Pageable pageable = PageRequest.of(page != null ? page - 1 : 0, pageSize != null ? pageSize : 20);
        Page<Bus> busPage;
        
        if (operatorId != null && isActive != null) {
            busPage = busRepository.findByOperatorIdAndIsActive(operatorId, isActive, pageable);
        } else if (operatorId != null) {
            busPage = busRepository.findByOperatorId(operatorId, pageable);
        } else if (isActive != null) {
            busPage = busRepository.findByIsActive(isActive, pageable);
        } else if (seatType != null) {
            busPage = busRepository.findBySeatType(seatType, pageable);
        } else {
            busPage = busRepository.findAll(pageable);
        }
        
        return BusListResponse.builder()
                .buses(busPage.getContent().stream()
                        .map(busMapper::toResponseWithDateTime)
                        .collect(Collectors.toList()))
                .total_count(busPage.getTotalElements())
                .page(page != null ? page : 1)
                .page_size(busPage.getSize())
                .build();
    }
}
