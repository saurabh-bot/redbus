package com.redbus.booking.constants;

public final class BookingConstants {
    
    private BookingConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    public static final String TRIP_ID_KEY = "trip_id";
    public static final String BOARDING_CITY_NAME_KEY = "boarding_city_name";
    public static final String DROPPING_CITY_NAME_KEY = "dropping_city_name";
    public static final String BOOKED_FROM_STOP_SEQUENCE_KEY = "booked_from_stop_sequence";
    public static final String BOOKED_TO_STOP_SEQUENCE_KEY = "booked_to_stop_sequence";
    public static final String BOARDING_DATETIME_KEY = "boarding_datetime";
    public static final String DROPPING_DATETIME_KEY = "dropping_datetime";
    
    public static final String SEAT_NUMBER_KEY = "seat_number";
    public static final String TRIP_ID_RESPONSE_KEY = "trip_id";
    public static final String BOARDING_DETAILS_KEY = "boarding_details";
    public static final String DROPPING_DETAILS_KEY = "dropping_details";
    public static final String STOP_SEQUENCE_KEY = "stop_sequence";
    public static final String PASSENGERS_KEY = "passengers";
    
    public static final String BOOKING_REFERENCE_ID_KEY = "booking_reference_id";
    public static final String PAYMENT_REFERENCE_ID_KEY = "payment_reference_id";
    
    public static final double DEFAULT_TOTAL_AMOUNT = 0.0;
    
    public static final String ERROR_BOOKING_NOT_FOUND = "Booking not found with reference: %s";
    public static final String ERROR_INVALID_STATUS_FOR_CONFIRM = "Only INITIATED bookings can be confirmed. Current status: %s";
    public static final String ERROR_INVALID_STATUS_FOR_CANCEL = "Only COMPLETED bookings can be cancelled. Current status: %s";
    public static final String ERROR_TRIP_DETAILS_SERIALIZATION = "Failed to serialize trip details for booking";
}

