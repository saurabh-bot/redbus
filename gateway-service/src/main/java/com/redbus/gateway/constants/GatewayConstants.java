package com.redbus.gateway.constants;

public final class GatewayConstants {
    
    private GatewayConstants() {
        throw new AssertionError("Constants class should not be instantiated");
    }
    
    public static final String BOOKING_REFERENCE_ID_KEY = "booking_reference_id";
    public static final String PAYMENT_REFERENCE_ID_KEY = "payment_reference_id";
    public static final String TRIP_ID_KEY = "trip_id";
    public static final String SEAT_NUMBERS_KEY = "seat_numbers";
    public static final String FROM_STOP_SEQUENCE_KEY = "from_stop_sequence";
    public static final String TO_STOP_SEQUENCE_KEY = "to_stop_sequence";
    public static final String USER_ID_KEY = "user_id";
    public static final String SEAT_NUMBER_KEY = "seat_number";
    
    public static final String BOARDING_DETAILS_KEY = "boarding_details";
    public static final String DROPPING_DETAILS_KEY = "dropping_details";
    public static final String STOP_SEQUENCE_KEY = "stop_sequence";
    public static final String PASSENGERS_KEY = "passengers";
    
    public static final String API_BOOKINGS_PATH = "/api/v1/bookings";
    public static final String API_BOOKINGS_REFERENCE_PATH = "/api/v1/bookings/reference/";
    public static final String API_BOOKINGS_CONFIRM_PATH = "/api/v1/bookings/confirm";
    public static final String API_BOOKINGS_CANCEL_PATH = "/api/v1/bookings/cancel";
    
    public static final String API_SEATS_CONFIRM_PATH = "/api/v1/seats/confirm";
    public static final String API_SEATS_CANCEL_PATH = "/api/v1/seats/cancel";
    public static final String API_SEATS_AVAILABILITY_PATH = "/api/v1/seats/availability";
    public static final String API_SEATS_LOCK_PATH = "/api/v1/seats/lock";
    
    public static final String ERROR_BOOKING_NOT_FOUND = "Booking not found with reference: %s";
    public static final String ERROR_TRIP_ID_MISSING = "Booking response does not include trip_id";
    public static final String ERROR_BOARDING_DROPPING_MISSING = "Booking does not have complete boarding/dropping details";
    public static final String ERROR_NO_PASSENGERS = "Booking does not have passengers";
}

