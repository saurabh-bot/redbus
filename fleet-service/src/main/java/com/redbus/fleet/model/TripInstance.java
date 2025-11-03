package com.redbus.fleet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.redbus.fleet.model.enums.TripStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "trip_instances",
       indexes = {
           @Index(name = "idx_trip_instances_route_id", columnList = "route_id"),
           @Index(name = "idx_trip_instances_date", columnList = "date"),
           @Index(name = "idx_trip_instances_route_date", columnList = "route_id, date")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripInstance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;
    
    @Column(name = "route_id", nullable = false)
    private Long routeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", insertable = false, updatable = false)
    @JsonIgnore
    private Route route;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private TripStatus status = TripStatus.SCHEDULED;
    
    @Column(name = "start_datetime_epoch")
    private Long startDatetimeEpoch;
    
    @Column(name = "end_datetime_epoch")
    private Long endDatetimeEpoch;
    
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
