package com.redbus.gateway.controller;

import com.redbus.gateway.config.BookingServiceConfig;
import com.redbus.gateway.config.FleetServiceConfig;
import com.redbus.gateway.constants.GatewayConstants;
import com.redbus.gateway.util.BookingExtractor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Booking Gateway", description = "Gateway API for booking operations")
public class BookingController {

    private final BookingServiceConfig bookingServiceConfig;
    private final FleetServiceConfig fleetServiceConfig;
    private final RestTemplate restTemplate;
    private final BookingExtractor bookingExtractor;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BookingController.class);
    
    public BookingController(BookingServiceConfig bookingServiceConfig, FleetServiceConfig fleetServiceConfig, 
                            RestTemplate restTemplate, BookingExtractor bookingExtractor) {
        this.bookingServiceConfig = bookingServiceConfig;
        this.fleetServiceConfig = fleetServiceConfig;
        this.restTemplate = restTemplate;
        this.bookingExtractor = bookingExtractor;
    }

    @PostMapping
    @Operation(summary = "Create booking", description = "Create booking via gateway (proxies to booking service)")
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> request) {
        String bookingServiceUrl = bookingServiceConfig.getUrl() + GatewayConstants.API_BOOKINGS_PATH;
        
        try {
            ResponseEntity<Map> response = callBookingService(bookingServiceUrl, HttpMethod.POST, request);
            log.info("Booking created via gateway. User: {}, Trip: {}", 
                    request.get(GatewayConstants.USER_ID_KEY), request.get(GatewayConstants.TRIP_ID_KEY));
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Failed to create booking via gateway. User: {}, Trip: {}", 
                    request.get(GatewayConstants.USER_ID_KEY), request.get(GatewayConstants.TRIP_ID_KEY), e);
            return buildErrorResponse("Failed to communicate with booking service: " + e.getMessage());
        }
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirm booking", description = "Orchestrates booking confirmation: confirms seats in Fleet Service, confirms booking in Booking Service, and confirms payment in Payment Service")
    public ResponseEntity<?> confirmBooking(@RequestBody Map<String, Object> request) {
        String bookingReferenceId = (String) request.get(GatewayConstants.BOOKING_REFERENCE_ID_KEY);
        String paymentReferenceId = (String) request.get(GatewayConstants.PAYMENT_REFERENCE_ID_KEY);
        
        if (bookingReferenceId == null || bookingReferenceId.isEmpty()) {
            return buildErrorResponse("booking_reference_id is required");
        }
        
        if (paymentReferenceId == null || paymentReferenceId.isEmpty()) {
            return buildErrorResponse("payment_reference_id is required");
        }
        
        try {
            Map<String, Object> booking = fetchBooking(bookingReferenceId);
            BookingExtractor.BookingDetails details = bookingExtractor.extractBookingDetails(booking, bookingReferenceId);
            
            // TODO: Confirm payment in Payment Service when available
            
            confirmSeatsInFleet(details);
            ResponseEntity<Map> bookingConfirmResponse = confirmBookingInService(bookingReferenceId, paymentReferenceId);
            
            log.info("Booking confirmed successfully via gateway. Reference: {}", bookingReferenceId);
            return ResponseEntity.status(bookingConfirmResponse.getStatusCode()).body(bookingConfirmResponse.getBody());
            
        } catch (IllegalArgumentException e) {
            log.error("Validation error during booking confirmation. Reference: {}", bookingReferenceId, e);
            return ResponseEntity.status(400).body(Map.of("error", "Bad Request", "message", e.getMessage()));
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Client error during booking confirmation. Reference: {}", bookingReferenceId, e);
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", "Client Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to confirm booking via gateway. Reference: {}", bookingReferenceId, e);
            return buildErrorResponse("Failed to confirm booking: " + e.getMessage());
        }
    }

    @PutMapping("/cancel")
    @Operation(summary = "Cancel booking", description = "Orchestrates booking cancellation: cancels seats in Fleet Service, processes payment refund in Payment Service, and cancels booking in Booking Service")
    public ResponseEntity<?> cancelBooking(@RequestBody Map<String, Object> request) {
        String bookingReferenceId = (String) request.get(GatewayConstants.BOOKING_REFERENCE_ID_KEY);
        
        if (bookingReferenceId == null || bookingReferenceId.isEmpty()) {
            return buildErrorResponse("booking_reference_id is required");
        }
        
        try {
            Map<String, Object> booking = fetchBooking(bookingReferenceId);
            BookingExtractor.BookingDetails details = bookingExtractor.extractBookingDetails(booking, bookingReferenceId);
            
            cancelSeatsInFleet(details);
            
            // TODO: Process payment refund in Payment Service when available
            
            ResponseEntity<Map> bookingCancelResponse = cancelBookingInService(bookingReferenceId);
            
            log.info("Booking cancelled successfully via gateway. Reference: {}", bookingReferenceId);
            return ResponseEntity.status(bookingCancelResponse.getStatusCode()).body(bookingCancelResponse.getBody());
            
        } catch (IllegalArgumentException e) {
            log.error("Validation error during booking cancellation. Reference: {}", bookingReferenceId, e);
            return ResponseEntity.status(400).body(Map.of("error", "Bad Request", "message", e.getMessage()));
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            log.error("Client error during booking cancellation. Reference: {}", bookingReferenceId, e);
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", "Client Error", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Failed to cancel booking via gateway. Reference: {}", bookingReferenceId, e);
            return buildErrorResponse("Failed to cancel booking: " + e.getMessage());
        }
    }
    
    private Map<String, Object> fetchBooking(String bookingReferenceId) {
        String getBookingUrl = bookingServiceConfig.getUrl() + GatewayConstants.API_BOOKINGS_REFERENCE_PATH + bookingReferenceId;
        ResponseEntity<Map> bookingResponse = restTemplate.getForEntity(getBookingUrl, Map.class);
        Map<String, Object> booking = bookingResponse.getBody();
        
        if (booking == null) {
            log.error("Booking not found. Reference: {}", bookingReferenceId);
            throw new IllegalArgumentException(String.format(GatewayConstants.ERROR_BOOKING_NOT_FOUND, bookingReferenceId));
        }
        return booking;
    }
    
    private void confirmSeatsInFleet(BookingExtractor.BookingDetails details) {
        String confirmSeatsUrl = fleetServiceConfig.getServiceUrl() + GatewayConstants.API_SEATS_CONFIRM_PATH;
        Map<String, Object> confirmSeatsRequest = Map.of(
                GatewayConstants.TRIP_ID_KEY, details.getTripId(),
                GatewayConstants.SEAT_NUMBERS_KEY, details.getSeatNumbers(),
                GatewayConstants.FROM_STOP_SEQUENCE_KEY, details.getFromStopSequence(),
                GatewayConstants.TO_STOP_SEQUENCE_KEY, details.getToStopSequence()
        );
        callFleetService(confirmSeatsUrl, HttpMethod.POST, confirmSeatsRequest);
    }
    
    private void cancelSeatsInFleet(BookingExtractor.BookingDetails details) {
        String cancelSeatsUrl = fleetServiceConfig.getServiceUrl() + GatewayConstants.API_SEATS_CANCEL_PATH;
        Map<String, Object> cancelSeatsRequest = Map.of(
                GatewayConstants.TRIP_ID_KEY, details.getTripId(),
                GatewayConstants.SEAT_NUMBERS_KEY, details.getSeatNumbers(),
                GatewayConstants.FROM_STOP_SEQUENCE_KEY, details.getFromStopSequence(),
                GatewayConstants.TO_STOP_SEQUENCE_KEY, details.getToStopSequence()
        );
        callFleetService(cancelSeatsUrl, HttpMethod.POST, cancelSeatsRequest);
    }
    
    private ResponseEntity<Map> confirmBookingInService(String bookingReferenceId, String paymentReferenceId) {
        String confirmBookingUrl = bookingServiceConfig.getUrl() + GatewayConstants.API_BOOKINGS_CONFIRM_PATH;
        Map<String, Object> confirmBookingRequest = Map.of(
                GatewayConstants.BOOKING_REFERENCE_ID_KEY, bookingReferenceId,
                GatewayConstants.PAYMENT_REFERENCE_ID_KEY, paymentReferenceId
        );
        return callBookingService(confirmBookingUrl, HttpMethod.POST, confirmBookingRequest);
    }
    
    private ResponseEntity<Map> cancelBookingInService(String bookingReferenceId) {
        String cancelBookingUrl = bookingServiceConfig.getUrl() + GatewayConstants.API_BOOKINGS_CANCEL_PATH;
        Map<String, Object> cancelBookingRequest = Map.of(
                GatewayConstants.BOOKING_REFERENCE_ID_KEY, bookingReferenceId
        );
        return callBookingService(cancelBookingUrl, HttpMethod.PUT, cancelBookingRequest);
    }
    
    private ResponseEntity<Map> callBookingService(String url, HttpMethod method, Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        return restTemplate.exchange(url, method, httpEntity, Map.class);
    }
    
    private void callFleetService(String url, HttpMethod method, Map<String, Object> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request, headers);
        restTemplate.exchange(url, method, httpEntity, Map.class);
    }
    
    private ResponseEntity<Map> buildErrorResponse(String message) {
        return ResponseEntity.status(500).body(Map.of(
                "error", "Internal Server Error",
                "message", message
        ));
    }
}

