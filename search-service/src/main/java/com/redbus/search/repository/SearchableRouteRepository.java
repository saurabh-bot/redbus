package com.redbus.search.repository;

import com.redbus.search.model.SearchableRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SearchableRouteRepository extends JpaRepository<SearchableRoute, Long> {
    
    // Query by city_code - travel_date is set to boarding/departure date
    // Users search by the date they want to board, not arrival date
    @Query("SELECT sr FROM SearchableRoute sr WHERE " +
           "sr.sourceCityCode = :sourceCityCode " +
           "AND sr.destinationCityCode = :destinationCityCode " +
           "AND sr.isActive = true " +
           "AND sr.seatCount > 0 " +
           "AND sr.travelDate = :travelDate")
    Page<SearchableRoute> findAvailableBusesByCityCode(@Param("sourceCityCode") String sourceCityCode,
                                                        @Param("destinationCityCode") String destinationCityCode,
                                                        @Param("travelDate") LocalDate travelDate,
                                                        Pageable pageable);
    
    // Query with AC filter
    @Query("SELECT sr FROM SearchableRoute sr WHERE " +
           "sr.sourceCityCode = :sourceCityCode " +
           "AND sr.destinationCityCode = :destinationCityCode " +
           "AND sr.isActive = true " +
           "AND sr.seatCount > 0 " +
           "AND sr.travelDate = :travelDate " +
           "AND sr.isAc = :isAc")
    Page<SearchableRoute> findBusesWithAcFilter(@Param("sourceCityCode") String sourceCityCode,
                                                @Param("destinationCityCode") String destinationCityCode,
                                                @Param("travelDate") LocalDate travelDate,
                                                @Param("isAc") Boolean isAc,
                                                Pageable pageable);
    
    // Query with seat type filter
    @Query("SELECT sr FROM SearchableRoute sr WHERE " +
           "sr.sourceCityCode = :sourceCityCode " +
           "AND sr.destinationCityCode = :destinationCityCode " +
           "AND sr.isActive = true " +
           "AND sr.seatCount > 0 " +
           "AND sr.travelDate = :travelDate " +
           "AND sr.seatType = :seatType")
    Page<SearchableRoute> findBusesWithSeatTypeFilter(@Param("sourceCityCode") String sourceCityCode,
                                                       @Param("destinationCityCode") String destinationCityCode,
                                                       @Param("travelDate") LocalDate travelDate,
                                                       @Param("seatType") String seatType,
                                                       Pageable pageable);
    
    // Query with both AC and seat type filters
    @Query("SELECT sr FROM SearchableRoute sr WHERE " +
           "sr.sourceCityCode = :sourceCityCode " +
           "AND sr.destinationCityCode = :destinationCityCode " +
           "AND sr.isActive = true " +
           "AND sr.seatCount > 0 " +
           "AND sr.travelDate = :travelDate " +
           "AND sr.isAc = :isAc " +
           "AND sr.seatType = :seatType")
    Page<SearchableRoute> findBusesWithAcAndSeatTypeFilter(@Param("sourceCityCode") String sourceCityCode,
                                                           @Param("destinationCityCode") String destinationCityCode,
                                                           @Param("travelDate") LocalDate travelDate,
                                                           @Param("isAc") Boolean isAc,
                                                           @Param("seatType") String seatType,
                                                           Pageable pageable);
    
    List<SearchableRoute> findByTripId(Long tripId);
    
    void deleteByTripId(Long tripId);
}
