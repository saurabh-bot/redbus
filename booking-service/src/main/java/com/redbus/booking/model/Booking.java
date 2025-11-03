package com.redbus.booking.model;

import com.redbus.booking.model.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "bookings",
       indexes = {
           @Index(name = "idx_bookings_user_id", columnList = "user_id"),
           @Index(name = "idx_bookings_status", columnList = "status"),
           @Index(name = "idx_bookings_reference", columnList = "booking_reference", unique = true)
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;
    
    @Column(name = "booking_reference", unique = true, length = 50)
    private String bookingReference;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "payment_reference_id", length = 100)
    private String paymentReferenceId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private BookingStatus status = BookingStatus.INITIATED;
    
    @Column(name = "trip_details", columnDefinition = "JSONB")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private String tripDetails; // JSON string containing trip_id, boarding_city_name, dropping_city_name, 
                                // booked_from_stop_sequence, booked_to_stop_sequence, boarding_datetime, dropping_datetime
    
    @Column(name = "total_amount")
    private Double totalAmount;
    
    @Column(name = "created_at_epoch", nullable = false)
    private Long createdAtEpoch;
    
    @Column(name = "updated_at_epoch")
    private Long updatedAtEpoch;
    
    @PrePersist
    protected void onCreate() {
        createdAtEpoch = Instant.now().getEpochSecond();
        updatedAtEpoch = createdAtEpoch;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAtEpoch = Instant.now().getEpochSecond();
    }
}
