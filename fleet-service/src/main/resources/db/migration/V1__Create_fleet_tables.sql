-- Fleet Service Database Schema

-- Cities Table
CREATE TABLE IF NOT EXISTS cities (
    city_id BIGSERIAL PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT uk_city_name UNIQUE (city_name, state, country)
);

CREATE INDEX idx_cities_name ON cities(city_name);

-- Buses Table
CREATE TABLE IF NOT EXISTS buses (
    bus_id BIGSERIAL PRIMARY KEY,
    operator_id BIGINT NOT NULL,
    bus_number VARCHAR(50) NOT NULL UNIQUE,
    seat_type VARCHAR(20) NOT NULL CHECK (seat_type IN ('SEATER', 'SLEEPER')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_ac BOOLEAN NOT NULL DEFAULT false,
    amenities JSONB,
    total_seats INTEGER,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT
);

CREATE INDEX idx_buses_operator ON buses(operator_id);
CREATE INDEX idx_buses_active ON buses(is_active);
CREATE INDEX idx_buses_seat_type ON buses(seat_type);

-- Routes Table
CREATE TABLE IF NOT EXISTS routes (
    route_id BIGSERIAL PRIMARY KEY,
    bus_id BIGINT NOT NULL,
    source_city_id BIGINT NOT NULL,
    destination_city_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    distance_km DOUBLE PRECISION,
    estimated_duration_minutes INTEGER,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_routes_bus FOREIGN KEY (bus_id) REFERENCES buses(bus_id),
    CONSTRAINT fk_routes_source_city FOREIGN KEY (source_city_id) REFERENCES cities(city_id),
    CONSTRAINT fk_routes_destination_city FOREIGN KEY (destination_city_id) REFERENCES cities(city_id)
);

CREATE INDEX idx_routes_bus_id ON routes(bus_id);
CREATE INDEX idx_routes_cities ON routes(source_city_id, destination_city_id);
CREATE INDEX idx_routes_active ON routes(is_active);

-- Route Stops Table
CREATE TABLE IF NOT EXISTS route_stops (
    route_stop_id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    sequence INTEGER NOT NULL,
    is_boarding_point BOOLEAN NOT NULL DEFAULT true,
    is_dropping_point BOOLEAN NOT NULL DEFAULT true,
    arrival_offset_minutes INTEGER,
    departure_offset_minutes INTEGER,
    fare_from_start DOUBLE PRECISION DEFAULT 0.0,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_route_stops_route FOREIGN KEY (route_id) REFERENCES routes(route_id),
    CONSTRAINT fk_route_stops_city FOREIGN KEY (city_id) REFERENCES cities(city_id),
    CONSTRAINT uk_route_stops_sequence UNIQUE (route_id, sequence)
);

CREATE INDEX idx_route_stops_route_id ON route_stops(route_id);
CREATE INDEX idx_route_stops_sequence ON route_stops(route_id, sequence);
CREATE INDEX idx_route_stops_city ON route_stops(city_id);

-- Trip Instances Table
CREATE TABLE IF NOT EXISTS trip_instances (
    trip_id BIGSERIAL PRIMARY KEY,
    route_id BIGINT NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED' CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    start_datetime_epoch BIGINT,
    end_datetime_epoch BIGINT,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_trip_instances_route FOREIGN KEY (route_id) REFERENCES routes(route_id),
    CONSTRAINT uk_trip_instances_route_date UNIQUE (route_id, date)
);

CREATE INDEX idx_trip_instances_route_id ON trip_instances(route_id);
CREATE INDEX idx_trip_instances_date ON trip_instances(date);
CREATE INDEX idx_trip_instances_route_date ON trip_instances(route_id, date);
CREATE INDEX idx_trip_instances_status ON trip_instances(status);

-- Seat Layouts Table
CREATE TABLE IF NOT EXISTS seat_layouts (
    seat_layout_id BIGSERIAL PRIMARY KEY,
    bus_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    seat_type VARCHAR(20) NOT NULL CHECK (seat_type IN ('WINDOW', 'MIDDLE', 'AISLE')),
    deck VARCHAR(20) NOT NULL CHECK (deck IN ('LOWER_DECK', 'UPPER_DECK')),
    row_number INTEGER,
    column_number INTEGER,
    position_x INTEGER,
    position_y INTEGER,
    base_price_multiplier DOUBLE PRECISION DEFAULT 1.0,
    is_ladies_seat BOOLEAN NOT NULL DEFAULT false,
    price DOUBLE PRECISION,
    CONSTRAINT fk_seat_layouts_bus FOREIGN KEY (bus_id) REFERENCES buses(bus_id),
    CONSTRAINT uk_seat_layouts_bus_seat UNIQUE (bus_id, seat_number)
);

CREATE INDEX idx_seat_layouts_bus_id ON seat_layouts(bus_id);
CREATE INDEX idx_seat_layouts_seat_number ON seat_layouts(bus_id, seat_number);

-- Seat Bookings Table
CREATE TABLE IF NOT EXISTS seat_bookings (
    seat_booking_id BIGSERIAL PRIMARY KEY,
    trip_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    booking_id BIGINT NOT NULL,
    booked_from_stop_sequence INTEGER NOT NULL,
    booked_to_stop_sequence INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'LOCKED' CHECK (status IN ('LOCKED', 'CONFIRMED', 'RELEASED', 'FAILED')),
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_seat_bookings_trip FOREIGN KEY (trip_id) REFERENCES trip_instances(trip_id),
    CONSTRAINT chk_seat_bookings_sequence CHECK (booked_from_stop_sequence < booked_to_stop_sequence)
);

CREATE INDEX idx_seat_bookings_trip_id ON seat_bookings(trip_id);
CREATE INDEX idx_seat_bookings_booking_id ON seat_bookings(booking_id);
CREATE INDEX idx_seat_bookings_trip_seat ON seat_bookings(trip_id, seat_number);
CREATE INDEX idx_seat_bookings_status ON seat_bookings(status);
CREATE INDEX idx_seat_bookings_stops ON seat_bookings(trip_id, seat_number, booked_from_stop_sequence, booked_to_stop_sequence);
