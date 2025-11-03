package com.redbus.fleet.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}

