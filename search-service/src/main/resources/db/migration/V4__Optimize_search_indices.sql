-- Optimize indices for Search Service
-- Analysis: Remove redundant single-column indices covered by composite indices

-- Remove redundant single-column indices (covered by composite indices)
-- idx_searchable_route_trip_id - REMOVE (not used, findByTripId being removed)
-- idx_searchable_route_bus_id - REMOVE (not used in queries, covered by trip_id lookup)
-- idx_searchable_route_active - REMOVE (covered by composite indices)
-- idx_searchable_route_source_city_code - REMOVE (covered by composite index)
-- idx_searchable_route_dest_city_code - REMOVE (covered by composite index)
-- idx_searchable_route_travel_date - REMOVE (covered by composite index)
-- idx_searchable_route_stop_sequences - REMOVE (not used in any queries)

DROP INDEX IF EXISTS idx_searchable_route_trip_id;
DROP INDEX IF EXISTS idx_searchable_route_bus_id;
DROP INDEX IF EXISTS idx_searchable_route_active;
DROP INDEX IF EXISTS idx_searchable_route_source_city_code;
DROP INDEX IF EXISTS idx_searchable_route_dest_city_code;
DROP INDEX IF EXISTS idx_searchable_route_travel_date;
DROP INDEX IF EXISTS idx_searchable_route_stop_sequences;
