package com.redbus.gateway.util;

import com.redbus.gateway.constants.GatewayConstants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BookingExtractor {
    
    public BookingDetails extractBookingDetails(Map<String, Object> booking, String bookingReferenceId) {
        Object tripIdObj = booking.get(GatewayConstants.TRIP_ID_KEY);
        if (tripIdObj == null) {
            log.error("Booking response missing trip_id. Reference: {}", bookingReferenceId);
            throw new IllegalArgumentException(GatewayConstants.ERROR_TRIP_ID_MISSING);
        }
        Long tripId = ((Number) tripIdObj).longValue();
        
        Map<String, Object> boardingDetails = (Map<String, Object>) booking.get(GatewayConstants.BOARDING_DETAILS_KEY);
        Map<String, Object> droppingDetails = (Map<String, Object>) booking.get(GatewayConstants.DROPPING_DETAILS_KEY);
        
        if (boardingDetails == null || droppingDetails == null) {
            log.error("Booking missing boarding/dropping details. Reference: {}", bookingReferenceId);
            throw new IllegalArgumentException(GatewayConstants.ERROR_BOARDING_DROPPING_MISSING);
        }
        
        Integer fromStopSequence = ((Number) boardingDetails.get(GatewayConstants.STOP_SEQUENCE_KEY)).intValue();
        Integer toStopSequence = ((Number) droppingDetails.get(GatewayConstants.STOP_SEQUENCE_KEY)).intValue();
        
        List<Map<String, Object>> passengers = (List<Map<String, Object>>) booking.get(GatewayConstants.PASSENGERS_KEY);
        if (passengers == null || passengers.isEmpty()) {
            log.error("Booking has no passengers. Reference: {}", bookingReferenceId);
            throw new IllegalArgumentException(GatewayConstants.ERROR_NO_PASSENGERS);
        }
        
        List<String> seatNumbers = passengers.stream()
                .map(p -> (String) p.get(GatewayConstants.SEAT_NUMBER_KEY))
                .collect(Collectors.toList());
        
        return BookingDetails.builder()
                .tripId(tripId)
                .seatNumbers(seatNumbers)
                .fromStopSequence(fromStopSequence)
                .toStopSequence(toStopSequence)
                .build();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDetails {
        private Long tripId;
        private List<String> seatNumbers;
        private Integer fromStopSequence;
        private Integer toStopSequence;
    }
}

