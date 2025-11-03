package com.redbus.booking.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class BookingReferenceGenerator {
    
    private static final String PREFIX = "REDBUS";
    private static final Random random = new Random();
    
    public String generateBookingReference() {
        // Format: REDBUS + YYYYMMDD + 6 random alphanumeric characters
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = generateRandomAlphanumeric(6);
        return PREFIX + datePart + randomPart;
    }
    
    private String generateRandomAlphanumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
