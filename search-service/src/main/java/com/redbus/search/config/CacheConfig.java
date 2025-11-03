package com.redbus.search.config;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Cache configuration for Search Service
 * 
 * Cache Names:
 * - search_results: Cached search results by query parameters
 * - searchable_routes: Cached route data
 */
@Component
public class CacheConfig {
    
    public static final String SEARCH_RESULTS_CACHE = "search_results";
    public static final String SEARCHABLE_ROUTES_CACHE = "searchable_routes";
    public static final String CITY_CACHE = "cities";
    
    /**
     * Example of how to use caching in service methods:
     * 
     * @Cacheable(value = SEARCH_RESULTS_CACHE, key = "#source + '-' + #destination + '-' + #travelDate")
     * public List<BusSearchResult> searchBuses(String source, String destination, String travelDate) {
     *     // Implementation
     * }
     */
}
