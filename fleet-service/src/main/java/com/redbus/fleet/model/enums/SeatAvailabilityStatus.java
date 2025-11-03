package com.redbus.fleet.model.enums;

public enum SeatAvailabilityStatus {
    AVAILABLE,  // Seat is available for booking
    LOCKED,     // Seat is temporarily locked (in Redis)
    BOOKED      // Seat is confirmed booked (in database)
}

