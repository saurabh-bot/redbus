package com.redbus.fleet.model;

import com.redbus.fleet.model.enums.Deck;
import com.redbus.fleet.model.enums.SeatType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat_layouts",
       indexes = {
           @Index(name = "idx_seat_layouts_bus_id", columnList = "bus_id"),
           @Index(name = "idx_seat_layouts_seat_number", columnList = "bus_id, seat_number")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_layout_id")
    private Long seatLayoutId;
    
    @Column(name = "bus_id", nullable = false)
    private Long busId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id", insertable = false, updatable = false)
    private Bus bus;
    
    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false, length = 20)
    private SeatType seatType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "deck", nullable = false, length = 20)
    private Deck deck;
    
    @Column(name = "row_number")
    private Integer rowNumber;
    
    @Column(name = "column_number")
    private Integer columnNumber;
    
    @Column(name = "position_x")
    private Integer positionX;
    
    @Column(name = "position_y")
    private Integer positionY;
    
    @Column(name = "base_price_multiplier")
    @Builder.Default
    private Double basePriceMultiplier = 1.0;
    
    @Column(name = "is_ladies_seat", nullable = false)
    @Builder.Default
    private Boolean isLadiesSeat = false;
    
    @Column(name = "price")
    private Double price;
}
