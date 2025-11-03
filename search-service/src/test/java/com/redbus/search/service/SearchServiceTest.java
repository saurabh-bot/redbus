package com.redbus.search.service;

import com.redbus.search.dto.BusSearchResult;
import com.redbus.search.dto.SearchBusesRequest;
import com.redbus.search.dto.SearchBusesResponse;
import com.redbus.search.model.SearchableRoute;
import com.redbus.search.repository.SearchableRouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchableRouteRepository searchableRouteRepository;

    @InjectMocks
    private SearchService searchService;

    private SearchBusesRequest request;
    private List<SearchableRoute> mockRoutes;
    private Page<SearchableRoute> mockPage;

    @BeforeEach
    void setUp() {
        request = SearchBusesRequest.builder()
                .source("DELHI")
                .destination("MUMBAI")
                .travelDate(LocalDate.of(2025, 11, 5))
                .build();

        mockRoutes = Arrays.asList(
                createSearchableRoute(100L, "RJ-14-AB-1234", 1200.0, 1000L, 12000L, true, "SEATER", 40),
                createSearchableRoute(101L, "RJ-14-CD-5678", 1500.0, 1100L, 13000L, true, "SLEEPER", 30)
        );

        mockPage = new PageImpl<>(mockRoutes, PageRequest.of(0, 20), 2);
    }

    @Test
    void testSearchBuses_NoFilters() {
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(2, response.getTotal_count());
        assertEquals(2, response.getBuses().size());
        assertEquals(1, response.getPage());
        assertEquals(20, response.getPage_size());

        BusSearchResult firstBus = response.getBuses().get(0);
        assertEquals(100L, firstBus.getTripId());
        assertEquals("RJ-14-AB-1234", firstBus.getBusNumber());
        assertEquals(1200.0, firstBus.getFare());
        assertEquals(40, firstBus.getAvailableSeats());

        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class));
    }

    @Test
    void testSearchBuses_WithAcFilter() {
        request.setIsAc(true);
        
        when(searchableRouteRepository.findBusesWithAcFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq(true), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(2, response.getBuses().size());
        verify(searchableRouteRepository).findBusesWithAcFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq(true), any(Pageable.class));
    }

    @Test
    void testSearchBuses_WithBusTypeFilter() {
        request.setBusType("SEATER");
        
        when(searchableRouteRepository.findBusesWithSeatTypeFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq("SEATER"), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(2, response.getBuses().size());
        verify(searchableRouteRepository).findBusesWithSeatTypeFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq("SEATER"), any(Pageable.class));
    }

    @Test
    void testSearchBuses_WithAcAndBusTypeFilter() {
        request.setIsAc(true);
        request.setBusType("SLEEPER");
        
        when(searchableRouteRepository.findBusesWithAcAndSeatTypeFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq(true), eq("SLEEPER"), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(2, response.getBuses().size());
        verify(searchableRouteRepository).findBusesWithAcAndSeatTypeFilter(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), eq(true), eq("SLEEPER"), any(Pageable.class));
    }

    @Test
    void testSearchBuses_SortByDeparture() {
        request.setSortBy("departure");
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), 
                argThat(pageable -> {
                    if (pageable instanceof PageRequest) {
                        PageRequest pr = (PageRequest) pageable;
                        return pr.getSort().stream()
                                .anyMatch(order -> order.getProperty().equals("boardingDatetime") && 
                                                  order.getDirection().isAscending());
                    }
                    return false;
                }));
    }

    @Test
    void testSearchBuses_SortByFare() {
        request.setSortBy("fare");
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), 
                argThat(pageable -> {
                    if (pageable instanceof PageRequest) {
                        PageRequest pr = (PageRequest) pageable;
                        return pr.getSort().stream()
                                .anyMatch(order -> order.getProperty().equals("fare") && 
                                                  order.getDirection().isAscending());
                    }
                    return false;
                }));
    }

    @Test
    void testSearchBuses_WithPagination() {
        request.setPage(2);
        request.setPageSize(10);
        
        Page<SearchableRoute> pageWithPagination = new PageImpl<>(
                Collections.singletonList(mockRoutes.get(0)), 
                PageRequest.of(1, 10), 
                2
        );
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(pageWithPagination);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(2, response.getPage());
        assertEquals(10, response.getPage_size());
    }

    @Test
    void testSearchBuses_MaxPageSizeLimit() {
        request.setPageSize(100); // Should be capped at 50
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()),
                argThat(pageable -> pageable.getPageSize() == 50));
    }

    @Test
    void testSearchBuses_EmptyResults() {
        Page<SearchableRoute> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 20), 0);
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(emptyPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(0, response.getTotal_count());
        assertEquals(0, response.getBuses().size());
    }

    @Test
    void testSearchBuses_LowercaseCityCodes() {
        request.setSource("delhi");
        request.setDestination("mumbai");
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class));
    }

    @Test
    void testSearchBuses_DefaultPageSize() {
        request.setPage(null);
        request.setPageSize(null);
        
        when(searchableRouteRepository.findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()), any(Pageable.class)))
                .thenReturn(mockPage);

        SearchBusesResponse response = searchService.searchBuses(request);

        assertNotNull(response);
        assertEquals(1, response.getPage());
        verify(searchableRouteRepository).findAvailableBusesByCityCode(
                eq("DELHI"), eq("MUMBAI"), eq(request.getTravelDate()),
                argThat(pageable -> pageable.getPageNumber() == 0 && pageable.getPageSize() == 20));
    }

    private SearchableRoute createSearchableRoute(Long tripId, String busNumber, Double fare, 
                                                   Long boardingEpoch, Long droppingEpoch,
                                                   Boolean isAc, String seatType, Integer seatCount) {
        String otherAttributes = String.format("{\"bus_number\": \"%s\"}", busNumber);
        
        return SearchableRoute.builder()
                .searchableRouteId(1L)
                .tripId(tripId)
                .routeId(10L)
                .sourceCityCode("DELHI")
                .destinationCityCode("MUMBAI")
                .fromStopSequence(1)
                .toStopSequence(5)
                .boardingDatetime(boardingEpoch)
                .droppingDatetime(droppingEpoch)
                .travelDate(LocalDate.of(2025, 11, 5))
                .seatCount(seatCount)
                .seatType(seatType)
                .busId(100L)
                .fare(fare)
                .hasWifi(true)
                .hasChargingPoint(true)
                .hasBlanket(false)
                .isAc(isAc)
                .otherAttributes(otherAttributes)
                .isActive(true)
                .build();
    }
}

