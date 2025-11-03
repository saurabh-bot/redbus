package com.redbus.booking.controller;

import com.redbus.booking.dto.*;
import com.redbus.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Management", description = "APIs for managing bookings")
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    @Operation(summary = "Create booking", description = "Creates a new booking with LOCKED status")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/reference/{bookingReferenceId}")
    @Operation(summary = "Get booking by reference", description = "Retrieves booking details by booking reference ID")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable("bookingReferenceId") String bookingReferenceId) {
        BookingResponse response = bookingService.getBookingByReference(bookingReferenceId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/confirm")
    @Operation(summary = "Confirm booking", description = "Confirms booking after successful payment. Changes status from INITIATED to COMPLETED")
    public ResponseEntity<BookingResponse> confirmBooking(@Valid @RequestBody ConfirmBookingRequest request) {
        BookingResponse response = bookingService.confirmBooking(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/cancel")
    @Operation(summary = "Cancel booking", description = "Cancels a COMPLETED booking. Changes status to CANCELLED. Only COMPLETED bookings can be cancelled.")
    public ResponseEntity<BookingResponse> cancelBooking(@Valid @RequestBody CancelBookingRequest request) {
        BookingResponse response = bookingService.cancelBooking(request.getBookingReferenceId());
        return ResponseEntity.ok(response);
    }
}
