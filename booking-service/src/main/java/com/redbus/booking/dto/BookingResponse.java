package com.redbus.booking.dto;

import com.redbus.booking.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private String bookingReference;
    private Long userId;
    private Long tripId;
    private BookingStatus status;
    private Double totalAmount;
    private String paymentReferenceId;
    private LocalDateTime createdAt;
    private BookingDetails boardingDetails;
    private BookingDetails droppingDetails;
    private List<PassengerInfo> passengers;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDetails {
        private String cityName;
        private Integer stopSequence;
        private LocalDateTime datetime;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerInfo {
        private Long passengerId;
        private String seatNumber;
        private String name;
        private Integer age;
        private String gender;
        private String email;
        private String phoneNumber;
    }
    
    public static LocalDateTime epochToDateTime(Long epoch) {
        return epoch != null ? LocalDateTime.ofInstant(Instant.ofEpochSecond(epoch), java.time.ZoneOffset.UTC) : null;
    }
}
