package com.redbus.fleet.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redbus.fleet.dto.BusRequest;
import com.redbus.fleet.dto.BusResponse;
import com.redbus.fleet.model.Bus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface BusMapper {
    
    BusMapper INSTANCE = Mappers.getMapper(BusMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();
    
    default Bus toEntity(BusRequest request) {
        Bus bus = new Bus();
        bus.setOperatorId(request.getOperatorId());
        bus.setBusNumber(request.getBusNumber());
        bus.setSeatType(request.getSeatType());
        bus.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        bus.setIsAc(request.getIsAc() != null ? request.getIsAc() : false);
        bus.setTotalSeats(request.getTotalSeats());
        if (request.getAmenities() != null) {
            bus.setAmenities(amenitiesToString(request.getAmenities()));
        }
        return bus;
    }
    
    @org.mapstruct.Mapping(target = "amenities", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    BusResponse toResponse(Bus bus);
    
    default Map<String, Object> mapAmenities(String amenitiesJson) {
        if (amenitiesJson == null) {
            return null;
        }
        try {
            return objectMapper.readValue(amenitiesJson, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return null;
        }
    }
    
    default BusResponse toResponseWithDateTime(Bus bus) {
        if (bus == null) {
            return null;
        }
        BusResponse response = toResponse(bus);
        if (response != null) {
            response.setCreatedAt(BusResponse.epochToDateTime(bus.getCreatedAtEpoch()));
            response.setUpdatedAt(BusResponse.epochToDateTime(bus.getUpdatedAtEpoch()));
            response.setAmenities(mapAmenities(bus.getAmenities()));
        }
        return response;
    }
    
    @org.mapstruct.Mapping(target = "busId", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    void updateEntityFromRequest(BusRequest request, @MappingTarget Bus bus);
    
    default String amenitiesToString(Map<String, Object> amenities) {
        if (amenities == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(amenities);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize amenities", e);
        }
    }
}
