package com.redbus.fleet.exception;

public class SeatAlreadyLockedException extends RuntimeException {
    
    public SeatAlreadyLockedException(String message) {
        super(message);
    }
}

