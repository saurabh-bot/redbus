package com.redbus.fleet.mapper;

import com.redbus.fleet.dto.RouteStopRequest;
import com.redbus.fleet.dto.RouteStopResponse;
import com.redbus.fleet.model.City;
import com.redbus.fleet.model.RouteStop;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteStopMapper {
    
    RouteStopMapper INSTANCE = Mappers.getMapper(RouteStopMapper.class);
    
    @org.mapstruct.Mapping(target = "routeStopId", ignore = true)
    @org.mapstruct.Mapping(target = "routeId", ignore = true)
    @org.mapstruct.Mapping(target = "cityId", ignore = true)
    @org.mapstruct.Mapping(target = "route", ignore = true)
    @org.mapstruct.Mapping(target = "city", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    RouteStop toEntity(RouteStopRequest request);
    
    @org.mapstruct.Mapping(target = "cityName", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    RouteStopResponse toResponse(RouteStop routeStop);
    
    default RouteStopResponse toResponseWithDetails(RouteStop routeStop, City city) {
        if (routeStop == null) {
            return null;
        }
        RouteStopResponse response = toResponse(routeStop);
        if (response != null) {
            response.setCreatedAt(RouteStopResponse.epochToDateTime(routeStop.getCreatedAtEpoch()));
            response.setUpdatedAt(RouteStopResponse.epochToDateTime(routeStop.getUpdatedAtEpoch()));
            response.setCityId(routeStop.getCityId()); // Include city_id
            if (city != null) {
                response.setCityName(city.getCityName());
                response.setCityCode(city.getCityCode());
            }
        }
        return response;
    }
    
    @org.mapstruct.Mapping(target = "routeStopId", ignore = true)
    @org.mapstruct.Mapping(target = "routeId", ignore = true)
    @org.mapstruct.Mapping(target = "route", ignore = true)
    @org.mapstruct.Mapping(target = "city", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    void updateEntityFromRequest(RouteStopRequest request, @MappingTarget RouteStop routeStop);
}
