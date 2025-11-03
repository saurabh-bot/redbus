-- Search Service Database Schema

-- Searchable Routes Table (Denormalized for fast search)
CREATE TABLE IF NOT EXISTS searchable_routes (
    searchable_route_id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    route_id BIGINT NOT NULL,
    source_city_id BIGINT NOT NULL,
    destination_city_id BIGINT NOT NULL,
    boarding_datetime BIGINT NOT NULL,
    dropping_datetime BIGINT NOT NULL,
    travel_date DATE NOT NULL,
    seat_count INTEGER NOT NULL DEFAULT 0,
    seat_type VARCHAR(20) NOT NULL CHECK (seat_type IN ('SEATER', 'SLEEPER')),
    bus_id BIGINT NOT NULL,
    fare DOUBLE PRECISION NOT NULL,
    rating DOUBLE PRECISION,
    has_wifi BOOLEAN NOT NULL DEFAULT false,
    has_charging_point BOOLEAN NOT NULL DEFAULT false,
    has_blanket BOOLEAN NOT NULL DEFAULT false,
    is_ac BOOLEAN NOT NULL DEFAULT false,
    other_attributes JSONB,
    created_at_epoch BIGINT NOT NULL
);

-- Composite index for main search query (source, destination, date, active)
CREATE INDEX idx_searchable_route_source_dest_date ON searchable_routes(source_city_id, destination_city_id, travel_date, is_active);

-- Index for trip_id lookups
CREATE INDEX idx_searchable_route_trip_id ON searchable_routes(trip_id);

-- Index for bus_id lookups
CREATE INDEX idx_searchable_route_bus_id ON searchable_routes(bus_id);

-- Index for active routes
CREATE INDEX idx_searchable_route_active ON searchable_routes(is_active);

-- Composite index for filter queries (is_ac, seat_type, rating, is_active)
CREATE INDEX idx_searchable_route_filters ON searchable_routes(is_ac, seat_type, rating, is_active);

-- Index for travel_date range queries
CREATE INDEX idx_searchable_route_travel_date ON searchable_routes(travel_date);
