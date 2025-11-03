package com.redbus.booking.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redbus.booking.constants.BookingConstants;
import com.redbus.booking.dto.*;
import com.redbus.booking.exception.BookingNotFoundException;
import com.redbus.booking.exception.InvalidBookingStatusException;
import com.redbus.booking.exception.TripDetailsSerializationException;
import com.redbus.booking.model.Booking;
import com.redbus.booking.model.BookingPassenger;
import com.redbus.booking.model.enums.BookingStatus;
import com.redbus.booking.repository.BookingRepository;
import com.redbus.booking.repository.BookingPassengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final ObjectMapper objectMapper;
    
    public BookingResponse createBooking(CreateBookingRequest request) {
        String bookingReference = bookingReferenceGenerator.generateBookingReference();
        
        while (bookingRepository.findByBookingReference(bookingReference).isPresent()) {
            bookingReference = bookingReferenceGenerator.generateBookingReference();
        }
        
        double totalAmount = request.getTotalAmount() != null ? request.getTotalAmount() : BookingConstants.DEFAULT_TOTAL_AMOUNT;
        
        String tripDetailsJson = buildTripDetailsJson(request);
        
        Booking booking = Booking.builder()
                .bookingReference(bookingReference)
                .userId(request.getUserId())
                .status(BookingStatus.INITIATED)
                .tripDetails(tripDetailsJson)
                .totalAmount(totalAmount)
                .build();
        
        Booking savedBooking = bookingRepository.save(booking);
        
        if (request.getPassengers() != null && !request.getPassengers().isEmpty()) {
            List<BookingPassenger> passengers = request.getPassengers().stream()
                    .map(passengerReq -> BookingPassenger.builder()
                            .bookingId(savedBooking.getBookingId())
                            .seatNumber(passengerReq.getSeatNumber())
                            .name(passengerReq.getName())
                            .age(passengerReq.getAge())
                            .gender(passengerReq.getGender())
                            .email(passengerReq.getEmail())
                            .phoneNumber(passengerReq.getPhoneNumber())
                            .build())
                    .collect(Collectors.toList());
            
            bookingPassengerRepository.saveAll(passengers);
        }
        
        log.info("Booking created successfully. Reference: {}, User: {}", bookingReference, request.getUserId());
        return toBookingResponse(savedBooking);
    }
    
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String bookingReference) {
        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> {
                    log.error("Booking not found with reference: {}", bookingReference);
                    return new BookingNotFoundException(String.format(BookingConstants.ERROR_BOOKING_NOT_FOUND, bookingReference));
                });
        return toBookingResponse(booking);
    }
    
    public BookingResponse confirmBooking(ConfirmBookingRequest request) {
        Booking booking = bookingRepository.findByBookingReference(request.getBookingReferenceId())
                .orElseThrow(() -> {
                    log.error("Booking not found for confirmation. Reference: {}", request.getBookingReferenceId());
                    return new BookingNotFoundException(String.format(BookingConstants.ERROR_BOOKING_NOT_FOUND, request.getBookingReferenceId()));
                });
        
        if (booking.getStatus() != BookingStatus.INITIATED) {
            log.error("Booking confirmation failed. Invalid status. Reference: {}, Status: {}", 
                    request.getBookingReferenceId(), booking.getStatus());
            throw new InvalidBookingStatusException(String.format(BookingConstants.ERROR_INVALID_STATUS_FOR_CONFIRM, booking.getStatus()));
        }
        
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setPaymentReferenceId(request.getPaymentReferenceId());
        
        Booking confirmedBooking = bookingRepository.save(booking);
        log.info("Booking confirmed successfully. Reference: {}", request.getBookingReferenceId());
        return toBookingResponse(confirmedBooking);
    }
    
    public BookingResponse cancelBooking(String bookingReferenceId) {
        Booking booking = bookingRepository.findByBookingReference(bookingReferenceId)
                .orElseThrow(() -> {
                    log.error("Booking not found for cancellation. Reference: {}", bookingReferenceId);
                    return new BookingNotFoundException(String.format(BookingConstants.ERROR_BOOKING_NOT_FOUND, bookingReferenceId));
                });
        
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            log.error("Booking cancellation failed. Invalid status. Reference: {}, Status: {}", 
                    bookingReferenceId, booking.getStatus());
            throw new InvalidBookingStatusException(String.format(BookingConstants.ERROR_INVALID_STATUS_FOR_CANCEL, booking.getStatus()));
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        Booking cancelledBooking = bookingRepository.save(booking);
        log.info("Booking cancelled successfully. Reference: {}", bookingReferenceId);
        return toBookingResponse(cancelledBooking);
    }
    
    private BookingResponse toBookingResponse(Booking booking) {
        BookingResponse.BookingDetails boardingDetails = null;
        BookingResponse.BookingDetails droppingDetails = null;
        Long tripId = null;
        
        if (booking.getTripDetails() != null) {
            try {
                Map<String, Object> tripDetails = objectMapper.readValue(booking.getTripDetails(), Map.class);
                
                if (tripDetails.containsKey(BookingConstants.TRIP_ID_KEY)) {
                    Object tripIdValue = tripDetails.get(BookingConstants.TRIP_ID_KEY);
                    tripId = Long.valueOf(tripIdValue.toString());
                }
                
                if (tripDetails.containsKey(BookingConstants.BOARDING_CITY_NAME_KEY) && 
                    tripDetails.containsKey(BookingConstants.BOOKED_FROM_STOP_SEQUENCE_KEY) &&
                    tripDetails.containsKey(BookingConstants.BOARDING_DATETIME_KEY)) {
                    boardingDetails = BookingResponse.BookingDetails.builder()
                            .cityName((String) tripDetails.get(BookingConstants.BOARDING_CITY_NAME_KEY))
                            .stopSequence(((Number) tripDetails.get(BookingConstants.BOOKED_FROM_STOP_SEQUENCE_KEY)).intValue())
                            .datetime(parseDateTime((String) tripDetails.get(BookingConstants.BOARDING_DATETIME_KEY)))
                            .build();
                }
                
                if (tripDetails.containsKey(BookingConstants.DROPPING_CITY_NAME_KEY) && 
                    tripDetails.containsKey(BookingConstants.BOOKED_TO_STOP_SEQUENCE_KEY) &&
                    tripDetails.containsKey(BookingConstants.DROPPING_DATETIME_KEY)) {
                    droppingDetails = BookingResponse.BookingDetails.builder()
                            .cityName((String) tripDetails.get(BookingConstants.DROPPING_CITY_NAME_KEY))
                            .stopSequence(((Number) tripDetails.get(BookingConstants.BOOKED_TO_STOP_SEQUENCE_KEY)).intValue())
                            .datetime(parseDateTime((String) tripDetails.get(BookingConstants.DROPPING_DATETIME_KEY)))
                            .build();
                }
            } catch (JsonProcessingException e) {
                log.error("Failed to parse trip_details for booking reference: {}", booking.getBookingReference(), e);
            }
        }
        
        List<BookingPassenger> passengers = bookingPassengerRepository.findByBookingId(booking.getBookingId());
        List<BookingResponse.PassengerInfo> passengerInfos = passengers.stream()
                .map(p -> BookingResponse.PassengerInfo.builder()
                        .passengerId(p.getPassengerId())
                        .seatNumber(p.getSeatNumber())
                        .name(p.getName())
                        .age(p.getAge())
                        .gender(p.getGender())
                        .email(p.getEmail())
                        .phoneNumber(p.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());
        
        return BookingResponse.builder()
                .bookingReference(booking.getBookingReference())
                .userId(booking.getUserId())
                .tripId(tripId)
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .paymentReferenceId(booking.getPaymentReferenceId())
                .createdAt(BookingResponse.epochToDateTime(booking.getCreatedAtEpoch()))
                .boardingDetails(boardingDetails)
                .droppingDetails(droppingDetails)
                .passengers(passengerInfos)
                .build();
    }
    
    private String buildTripDetailsJson(CreateBookingRequest request) {
        Map<String, Object> tripDetails = new HashMap<>();
        tripDetails.put(BookingConstants.TRIP_ID_KEY, request.getTripId().toString());
        tripDetails.put(BookingConstants.BOARDING_CITY_NAME_KEY, request.getBoardingCityName());
        tripDetails.put(BookingConstants.DROPPING_CITY_NAME_KEY, request.getDroppingCityName());
        tripDetails.put(BookingConstants.BOOKED_FROM_STOP_SEQUENCE_KEY, request.getBoardingStopSequence());
        tripDetails.put(BookingConstants.BOOKED_TO_STOP_SEQUENCE_KEY, request.getDroppingStopSequence());
        tripDetails.put(BookingConstants.BOARDING_DATETIME_KEY, request.getBoardingDatetime());
        tripDetails.put(BookingConstants.DROPPING_DATETIME_KEY, request.getDroppingDatetime());
        
        try {
            return objectMapper.writeValueAsString(tripDetails);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize trip details for booking", e);
            throw new TripDetailsSerializationException(BookingConstants.ERROR_TRIP_DETAILS_SERIALIZATION, e);
        }
    }
    
    private LocalDateTime parseDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeString);
        } catch (Exception e) {
            log.error("Failed to parse datetime: {}", dateTimeString, e);
            return null;
        }
    }
}
