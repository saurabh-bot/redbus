package com.redbus.fleet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "routes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;
    
    @Column(name = "bus_id", nullable = false)
    private Long busId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", insertable = false, updatable = false)
    private Bus bus;
    
    @Column(name = "source_city_id", nullable = false)
    private Long sourceCityId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_city_id", insertable = false, updatable = false)
    private City sourceCity;
    
    @Column(name = "destination_city_id", nullable = false)
    private Long destinationCityId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_city_id", insertable = false, updatable = false)
    private City destinationCity;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(name = "distance_km")
    private Double distanceKm;
    
    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;
    
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
