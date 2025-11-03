package com.redbus.booking.model;

import com.redbus.booking.model.enums.CancellationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "cancellations",
       indexes = {
           @Index(name = "idx_cancellations_booking_id", columnList = "booking_id"),
           @Index(name = "idx_cancellations_user_id", columnList = "user_id"),
           @Index(name = "idx_cancellations_status", columnList = "status")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cancellation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cancellation_id")
    private Long cancellationId;
    
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", insertable = false, updatable = false)
    private Booking booking;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "reason", length = 500)
    private String reason;
    
    @Column(name = "refund_amount")
    private Double refundAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private CancellationStatus status = CancellationStatus.PENDING;
    
    @Column(name = "cancelled_seats", columnDefinition = "JSONB")
    private String cancelledSeats; // JSON array of seat numbers cancelled
    
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
