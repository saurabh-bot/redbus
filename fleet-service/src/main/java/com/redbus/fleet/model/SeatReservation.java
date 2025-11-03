package com.redbus.fleet.model;

import com.redbus.fleet.model.enums.SeatReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "seat_reservations",
       indexes = {
           @Index(name = "idx_seat_reservations_trip_id", columnList = "trip_id"),
           @Index(name = "idx_seat_reservations_trip_seat", columnList = "trip_id, seat_number"),
           @Index(name = "idx_seat_reservations_stops", columnList = "trip_id, seat_number, booked_from_stop_sequence, booked_to_stop_sequence"),
           @Index(name = "idx_seat_reservations_status", columnList = "status")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_reservation_id")
    private Long seatReservationId;
    
    @Column(name = "trip_id", nullable = false)
    private Long tripId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", insertable = false, updatable = false)
    private TripInstance tripInstance;
    
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;
    
    @Column(name = "booked_from_stop_sequence", nullable = false)
    private Integer bookedFromStopSequence;
    
    @Column(name = "booked_to_stop_sequence", nullable = false)
    private Integer bookedToStopSequence;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private SeatReservationStatus status = SeatReservationStatus.CREATED;
    
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
