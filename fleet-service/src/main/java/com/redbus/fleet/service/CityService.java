package com.redbus.fleet.service;

import com.redbus.fleet.dto.CityListResponse;
import com.redbus.fleet.dto.CityRequest;
import com.redbus.fleet.dto.CityResponse;
import com.redbus.fleet.mapper.CityMapper;
import com.redbus.fleet.model.City;
import com.redbus.fleet.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CityService {
    
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;
    private final CityCodeGenerator cityCodeGenerator;
    
    public CityResponse createCity(CityRequest request) {
        log.info("Creating city: {}", request.getCityName());
        
        // Check if city already exists
        cityRepository.findByCityName(request.getCityName())
                .ifPresent(city -> {
                    throw new IllegalArgumentException("City with name " + request.getCityName() + " already exists");
                });
        
        City city = cityMapper.toEntity(request);
        
        // Generate unique city code
        String cityCode = generateUniqueCityCode(request.getCityName());
        city.setCityCode(cityCode);
        
        City savedCity = cityRepository.save(city);
        log.info("City created successfully with ID: {}, code: {}", savedCity.getCityId(), cityCode);
        return cityMapper.toResponseWithDateTime(savedCity);
    }
    
    private String generateUniqueCityCode(String cityName) {
        String baseCode = cityCodeGenerator.generateCityCode(cityName);
        String cityCode = baseCode;
        int suffix = 1;
        
        // Ensure uniqueness by appending number if needed
        while (cityRepository.findByCityCode(cityCode).isPresent()) {
            if (suffix == 1) {
                cityCode = baseCode + suffix;
            } else {
                // Replace last digit or append
                cityCode = baseCode.substring(0, Math.min(baseCode.length(), MAX_CODE_LENGTH - String.valueOf(suffix).length())) + suffix;
            }
            suffix++;
            
            if (suffix > 999) {
                throw new IllegalStateException("Unable to generate unique city code for: " + cityName);
            }
        }
        
        return cityCode;
    }
    
    private static final int MAX_CODE_LENGTH = 5;
    
    @Transactional(readOnly = true)
    public CityResponse getCityByCode(String cityCode) {
        log.info("Fetching city with code: {}", cityCode);
        City city = cityRepository.findByCityCode(cityCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("City not found with code: " + cityCode));
        return cityMapper.toResponseWithDateTime(city);
    }
    
    @Transactional(readOnly = true)
    public CityResponse getCityById(Long cityId) {
        log.info("Fetching city with ID: {}", cityId);
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + cityId));
        return cityMapper.toResponseWithDateTime(city);
    }
    
    public CityResponse updateCity(String cityCode, CityRequest request) {
        log.info("Updating city with code: {}", cityCode);
        City city = cityRepository.findByCityCode(cityCode.toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("City not found with code: " + cityCode));
        
        cityMapper.updateEntityFromRequest(request, city);
        City updatedCity = cityRepository.save(city);
        log.info("City updated successfully with code: {}", updatedCity.getCityCode());
        return cityMapper.toResponseWithDateTime(updatedCity);
    }
    
    @Transactional(readOnly = true)
    public CityListResponse getAllCities() {
        log.info("Fetching all cities");
        List<CityResponse> cities = cityRepository.findAll().stream()
                .map(cityMapper::toResponseWithDateTime)
                .collect(Collectors.toList());
        
        return CityListResponse.builder()
                .cities(cities)
                .total_count((long) cities.size())
                .build();
    }
}
