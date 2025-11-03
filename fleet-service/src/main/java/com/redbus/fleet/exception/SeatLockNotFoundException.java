package com.redbus.fleet.exception;

public class SeatLockNotFoundException extends RuntimeException {
    
    public SeatLockNotFoundException(String message) {
        super(message);
    }
}

