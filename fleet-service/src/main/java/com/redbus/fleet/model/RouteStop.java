package com.redbus.fleet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "route_stops", 
       indexes = {
           @Index(name = "idx_route_stops_route_id", columnList = "route_id"),
           @Index(name = "idx_route_stops_sequence", columnList = "route_id, sequence")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_stop_id")
    private Long routeStopId;
    
    @Column(name = "route_id", nullable = false)
    private Long routeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", insertable = false, updatable = false)
    private Route route;
    
    @Column(name = "city_id", nullable = false)
    private Long cityId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;
    
    @Column(name = "sequence", nullable = false)
    private Integer sequence;
    
    @Column(name = "is_boarding_point", nullable = false)
    @Builder.Default
    private Boolean isBoardingPoint = true;
    
    @Column(name = "is_dropping_point", nullable = false)
    @Builder.Default
    private Boolean isDroppingPoint = true;
    
    @Column(name = "arrival_offset_minutes")
    private Integer arrivalOffsetMinutes;
    
    @Column(name = "departure_offset_minutes")
    private Integer departureOffsetMinutes;
    
    @Column(name = "fare_from_start")
    @Builder.Default
    private Double fareFromStart = 0.0;
    
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
