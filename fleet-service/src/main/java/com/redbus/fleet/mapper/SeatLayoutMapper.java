package com.redbus.fleet.mapper;

import com.redbus.fleet.dto.SeatLayoutRequest;
import com.redbus.fleet.dto.SeatLayoutResponse;
import com.redbus.fleet.model.SeatLayout;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SeatLayoutMapper {
    
    SeatLayoutMapper INSTANCE = Mappers.getMapper(SeatLayoutMapper.class);
    
    @org.mapstruct.Mapping(target = "seatLayoutId", ignore = true)
    @org.mapstruct.Mapping(target = "busId", ignore = true)
    @org.mapstruct.Mapping(target = "bus", ignore = true)
    @org.mapstruct.Mapping(target = "isLadiesSeat", ignore = true)
    @org.mapstruct.Mapping(target = "basePriceMultiplier", ignore = true)
    SeatLayout toEntity(SeatLayoutRequest request);
    
    default SeatLayout toEntityWithDefaults(SeatLayoutRequest request) {
        SeatLayout seat = toEntity(request);
        if (seat != null) {
            seat.setIsLadiesSeat(request.getIsLadiesSeat() != null ? request.getIsLadiesSeat() : false);
            seat.setBasePriceMultiplier(request.getBasePriceMultiplier() != null ? request.getBasePriceMultiplier() : 1.0);
        }
        return seat;
    }
    
    SeatLayoutResponse toResponse(SeatLayout seatLayout);
    
    @org.mapstruct.Mapping(target = "seatLayoutId", ignore = true)
    @org.mapstruct.Mapping(target = "busId", ignore = true)
    @org.mapstruct.Mapping(target = "bus", ignore = true)
    void updateEntityFromRequest(SeatLayoutRequest request, @MappingTarget SeatLayout seatLayout);
}
