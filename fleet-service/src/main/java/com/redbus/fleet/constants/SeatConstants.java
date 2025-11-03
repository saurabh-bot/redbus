package com.redbus.fleet.constants;

public final class SeatConstants {
    
    private SeatConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    public static final String LOCK_KEY_PREFIX = "seat_lock:";
    public static final int LOCK_TTL_MINUTES = 15;
    public static final String LOCK_VALUE = "locked";
    
    public static final String LOCK_KEY_DELIMITER = ":";
    public static final int MIN_LOCK_KEY_PARTS = 5;
    
    public static final String ERROR_SEAT_ALREADY_LOCKED = "Seat %s is already locked for an overlapping route segment (trip: %d, segments: %d->%d)";
    public static final String ERROR_SEAT_ALREADY_BOOKED = "Seat %s is already booked for an overlapping route segment (trip: %d, segments: %d->%d)";
    public static final String ERROR_NO_LOCK_FOUND = "No lock found for seat %s on trip %d with sequences %d->%d. Cannot confirm.";
    public static final String ERROR_NO_RESERVATION_FOUND = "No reservation found for seat %s on trip %d with sequences %d->%d";
    public static final String ERROR_INVALID_RESERVATION_STATUS = "Seat %s reservation (trip %d, %d->%d) is already %s and cannot be cancelled";
}

