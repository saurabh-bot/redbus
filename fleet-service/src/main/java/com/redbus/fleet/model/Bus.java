package com.redbus.fleet.model;

import com.redbus.fleet.model.enums.BusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "buses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long busId;
    
    @Column(name = "operator_id", nullable = false)
    private Long operatorId;
    
    @Column(name = "bus_number", nullable = false, length = 50, unique = true)
    private String busNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false, length = 20)
    private BusType seatType;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(name = "is_ac", nullable = false)
    @Builder.Default
    private Boolean isAc = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "amenities", columnDefinition = "JSONB")
    private String amenities; // JSON string for amenities like wifi, blankets, etc.
    
    @Column(name = "total_seats")
    private Integer totalSeats;
    
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
