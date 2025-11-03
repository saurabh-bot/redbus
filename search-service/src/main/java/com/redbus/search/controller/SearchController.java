package com.redbus.search.controller;

import com.redbus.search.dto.SearchBusesRequest;
import com.redbus.search.dto.SearchBusesResponse;
import com.redbus.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "APIs for searching buses")
public class SearchController {
    
    private final SearchService searchService;
    
    @GetMapping("/buses")
    @Operation(summary = "Search buses", description = "Search available buses with filters")
    public ResponseEntity<SearchBusesResponse> searchBuses(
            @RequestParam("source") String source,
            @RequestParam("destination") String destination,
            @RequestParam("travel_date") java.time.LocalDate travelDate,
            @RequestParam(value = "is_ac", required = false) Boolean isAc,
            @RequestParam(value = "bus_type", required = false) String busType,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", required = false, defaultValue = "20") Integer pageSize) {
        SearchBusesRequest request = SearchBusesRequest.builder()
                .source(source)
                .destination(destination)
                .travelDate(travelDate)
                .isAc(isAc)
                .busType(busType)
                .sortBy(sortBy)
                .page(page)
                .pageSize(pageSize)
                .build();
        SearchBusesResponse response = searchService.searchBuses(request);
        return ResponseEntity.ok(response);
    }
}
