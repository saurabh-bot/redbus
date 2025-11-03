package com.redbus.fleet.service;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Generates unique city codes from city names.
 * Format: 3-5 uppercase letters (e.g., "DEL", "MUM", "BLR", "KOLK")
 */
@Component
public class CityCodeGenerator {
    
    private static final Pattern NON_ASCII = Pattern.compile("[^\\p{ASCII}]");
    private static final int MIN_CODE_LENGTH = 3;
    private static final int MAX_CODE_LENGTH = 5;
    
    /**
     * Generates a city code from city name.
     * Examples:
     * - "Delhi" -> "DEL"
     * - "Mumbai" -> "MUM"
     * - "Bangalore" -> "BLR"
     * - "Kolkata" -> "KOLK"
     * 
     * @param cityName The city name
     * @return Generated city code (3-5 uppercase letters)
     */
    public String generateCityCode(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty");
        }
        
        // Normalize (remove accents, special chars)
        String normalized = Normalizer.normalize(cityName.trim(), Normalizer.Form.NFD);
        normalized = NON_ASCII.matcher(normalized).replaceAll("");
        
        // Remove non-alphabetic characters
        normalized = normalized.replaceAll("[^A-Za-z]", "");
        
        if (normalized.length() < MIN_CODE_LENGTH) {
            throw new IllegalArgumentException("City name too short to generate code");
        }
        
        // Convert to uppercase
        normalized = normalized.toUpperCase();
        
        // Generate code based on length
        String code;
        if (normalized.length() <= MAX_CODE_LENGTH) {
            // Use full name if short enough
            code = normalized;
        } else {
            // Use first 3-5 letters, preferring vowels for better readability
            code = generateShortCode(normalized);
        }
        
        return code;
    }
    
    /**
     * Generates a short code from a long city name.
     * Prefers consonants for better readability.
     */
    private String generateShortCode(String normalized) {
        StringBuilder code = new StringBuilder();
        int length = Math.min(normalized.length(), MAX_CODE_LENGTH);
        
        // Take first character (usually consonant)
        if (normalized.length() > 0) {
            code.append(normalized.charAt(0));
        }
        
        // Add vowels and consonants to reach target length
        int vowelsAdded = 0;
        for (int i = 1; i < normalized.length() && code.length() < length; i++) {
            char c = normalized.charAt(i);
            if (isVowel(c) && vowelsAdded < 2) {
                code.append(c);
                vowelsAdded++;
            } else if (!isVowel(c)) {
                code.append(c);
            }
        }
        
        // If still too short, fill with remaining characters
        while (code.length() < MIN_CODE_LENGTH && code.length() < normalized.length()) {
            char c = normalized.charAt(code.length());
            if (!code.toString().contains(String.valueOf(c))) {
                code.append(c);
            }
        }
        
        return code.toString();
    }
    
    private boolean isVowel(char c) {
        return "AEIOU".indexOf(c) >= 0;
    }
}

