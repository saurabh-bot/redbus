package com.redbus.fleet.mapper;

import com.redbus.fleet.dto.RouteRequest;
import com.redbus.fleet.dto.RouteResponse;
import com.redbus.fleet.model.Route;
import com.redbus.fleet.repository.CityRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    
    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);
    
    @org.mapstruct.Mapping(target = "routeId", ignore = true)
    @org.mapstruct.Mapping(target = "bus", ignore = true)
    @org.mapstruct.Mapping(target = "sourceCity", ignore = true)
    @org.mapstruct.Mapping(target = "destinationCity", ignore = true)
    @org.mapstruct.Mapping(target = "sourceCityId", ignore = true)
    @org.mapstruct.Mapping(target = "destinationCityId", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    Route toEntity(RouteRequest request);
    
    @org.mapstruct.Mapping(target = "sourceCityCode", ignore = true)
    @org.mapstruct.Mapping(target = "destinationCityCode", ignore = true)
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    RouteResponse toResponse(Route route);
    
    default RouteResponse toResponseWithCityCodes(Route route, CityRepository cityRepository) {
        if (route == null) {
            return null;
        }
        RouteResponse response = toResponse(route);
        if (response != null) {
            response.setCreatedAt(RouteResponse.epochToDateTime(route.getCreatedAtEpoch()));
            response.setUpdatedAt(RouteResponse.epochToDateTime(route.getUpdatedAtEpoch()));
            
            // Set city codes
            cityRepository.findById(route.getSourceCityId())
                    .ifPresent(city -> response.setSourceCityCode(city.getCityCode()));
            cityRepository.findById(route.getDestinationCityId())
                    .ifPresent(city -> response.setDestinationCityCode(city.getCityCode()));
        }
        return response;
    }
    
    @org.mapstruct.Mapping(target = "routeId", ignore = true)
    @org.mapstruct.Mapping(target = "bus", ignore = true)
    @org.mapstruct.Mapping(target = "sourceCity", ignore = true)
    @org.mapstruct.Mapping(target = "destinationCity", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    void updateEntityFromRequest(RouteRequest request, @MappingTarget Route route);
}
