package com.redbus.fleet.repository;

import com.redbus.fleet.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByCityName(String cityName);
    Optional<City> findByCityCode(String cityCode);
}
