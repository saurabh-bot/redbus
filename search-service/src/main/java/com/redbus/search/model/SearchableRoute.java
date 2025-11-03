package com.redbus.search.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "searchable_routes",
       indexes = {
           @Index(name = "idx_searchable_route_source_dest_date", 
                  columnList = "source_city_code, destination_city_code, travel_date, is_active"),
           @Index(name = "idx_searchable_route_trip_id", columnList = "trip_id"),
           @Index(name = "idx_searchable_route_bus_id", columnList = "bus_id"),
           @Index(name = "idx_searchable_route_active", columnList = "is_active"),
           @Index(name = "idx_searchable_route_filters", 
                  columnList = "is_ac, seat_type, is_active")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchableRoute {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "searchable_route_id")
    private Long searchableRouteId;
    
    @Column(name = "trip_id", nullable = false)
    private Long tripId;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @Column(name = "route_id", nullable = false)
    private Long routeId;
    
    @Column(name = "source_city_id", nullable = false)
    private Long sourceCityId;
    
    @Column(name = "destination_city_id", nullable = false)
    private Long destinationCityId;
    
    @Column(name = "source_city_code", length = 10)
    private String sourceCityCode;
    
    @Column(name = "destination_city_code", length = 10)
    private String destinationCityCode;
    
    @Column(name = "from_stop_sequence")
    private Integer fromStopSequence;
    
    @Column(name = "to_stop_sequence")
    private Integer toStopSequence;
    
    @Column(name = "boarding_datetime", nullable = false)
    private Long boardingDatetime;
    
    @Column(name = "dropping_datetime", nullable = false)
    private Long droppingDatetime;
    
    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;
    
    @Column(name = "seat_count", nullable = false)
    @Builder.Default
    private Integer seatCount = 0;
    
    @Column(name = "seat_type", nullable = false, length = 20)
    private String seatType; // SEATER or SLEEPER
    
    @Column(name = "bus_id", nullable = false)
    private Long busId;
    
    @Column(name = "fare", nullable = false)
    private Double fare;
    
    @Column(name = "has_wifi", nullable = false)
    @Builder.Default
    private Boolean hasWifi = false;
    
    @Column(name = "has_charging_point", nullable = false)
    @Builder.Default
    private Boolean hasChargingPoint = false;
    
    @Column(name = "has_blanket", nullable = false)
    @Builder.Default
    private Boolean hasBlanket = false;
    
    @Column(name = "is_ac", nullable = false)
    @Builder.Default
    private Boolean isAc = false;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "other_attributes", columnDefinition = "JSONB")
    private String otherAttributes; // JSON string for additional UI attributes
    
    @Column(name = "created_at_epoch", nullable = false)
    private Long createdAtEpoch;
    
    @PrePersist
    protected void onCreate() {
        createdAtEpoch = Instant.now().getEpochSecond();
    }
}
