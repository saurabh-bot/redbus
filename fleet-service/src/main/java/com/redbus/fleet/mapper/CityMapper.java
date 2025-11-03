package com.redbus.fleet.mapper;

import com.redbus.fleet.dto.CityRequest;
import com.redbus.fleet.dto.CityResponse;
import com.redbus.fleet.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CityMapper {
    
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);
    
    @org.mapstruct.Mapping(target = "cityId", ignore = true)
    @org.mapstruct.Mapping(target = "cityCode", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    City toEntity(CityRequest request);
    
    @org.mapstruct.Mapping(target = "createdAt", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAt", ignore = true)
    CityResponse toResponse(City city);
    
    default CityResponse toResponseWithDateTime(City city) {
        if (city == null) {
            return null;
        }
        CityResponse response = toResponse(city);
        if (response != null) {
            response.setCreatedAt(CityResponse.epochToDateTime(city.getCreatedAtEpoch()));
            response.setUpdatedAt(CityResponse.epochToDateTime(city.getUpdatedAtEpoch()));
        }
        return response;
    }
    
    @org.mapstruct.Mapping(target = "cityId", ignore = true)
    @org.mapstruct.Mapping(target = "createdAtEpoch", ignore = true)
    @org.mapstruct.Mapping(target = "updatedAtEpoch", ignore = true)
    void updateEntityFromRequest(CityRequest request, @MappingTarget City city);
}
