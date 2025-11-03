-- SQL Script to populate searchable_routes table for all trip instances
-- This creates entries for all possible source-destination combinations from route stops

-- Connect to search_db and use a cross-database query or insert from fleet_db
-- This assumes both databases are on the same PostgreSQL instance

-- First, let's create a simple function to insert searchable routes
-- We'll use a DO block to iterate through trips and route stops

DO $$
DECLARE
    trip_rec RECORD;
    route_rec RECORD;
    bus_rec RECORD;
    stop_source RECORD;
    stop_dest RECORD;
    source_city_id_var BIGINT;
    dest_city_id_var BIGINT;
    boarding_epoch BIGINT;
    dropping_epoch BIGINT;
    fare_calc DOUBLE PRECISION;
    seat_type_var VARCHAR(20);
    is_ac_var BOOLEAN;
    total_seats_var INTEGER;
    has_wifi_var BOOLEAN;
    has_charging_var BOOLEAN;
    has_blanket_var BOOLEAN;
    rating_var DOUBLE PRECISION := 4.5;
    other_attrs JSONB;
    created_count INTEGER := 0;
BEGIN
    -- Loop through all trips for Route 1
    FOR trip_rec IN 
        SELECT trip_id, route_id, date, start_datetime_epoch
        FROM trip_instances
        WHERE route_id = 1
        AND start_datetime_epoch IS NOT NULL
        ORDER BY date
    LOOP
        -- Get route details (we know route_id = 1)
        SELECT bus_id INTO route_rec
        FROM routes
        WHERE route_id = trip_rec.route_id;
        
        -- Get bus details
        SELECT 
            seat_type,
            is_ac,
            total_seats,
            amenities
        INTO 
            seat_type_var,
            is_ac_var,
            total_seats_var,
            bus_rec
        FROM buses
        WHERE bus_id = (SELECT bus_id FROM routes WHERE route_id = trip_rec.route_id);
        
        -- Extract amenities from JSON
        SELECT 
            COALESCE((bus_rec->>'wifi')::BOOLEAN, false),
            COALESCE((bus_rec->>'charging_point')::BOOLEAN, false),
            COALESCE((bus_rec->>'blanket')::BOOLEAN, false)
        INTO has_wifi_var, has_charging_var, has_blanket_var;
        
        -- Get bus number for other_attributes
        SELECT bus_number INTO bus_rec
        FROM buses
        WHERE bus_id = (SELECT bus_id FROM routes WHERE route_id = trip_rec.route_id);
        
        other_attrs := jsonb_build_object('bus_number', bus_rec);
        
        -- Loop through all route stops to create combinations
        FOR stop_source IN
            SELECT 
                rs.route_stop_id,
                rs.city_id,
                rs.sequence,
                rs.departure_offset_minutes,
                rs.fare_from_start,
                rs.is_boarding_point,
                c.city_code
            FROM route_stops rs
            JOIN cities c ON rs.city_id = c.city_id
            WHERE rs.route_id = trip_rec.route_id
            AND rs.is_boarding_point = true
            ORDER BY rs.sequence
        LOOP
            FOR stop_dest IN
                SELECT 
                    rs.route_stop_id,
                    rs.city_id,
                    rs.sequence,
                    rs.arrival_offset_minutes,
                    rs.fare_from_start,
                    rs.is_dropping_point,
                    c.city_code
                FROM route_stops rs
                JOIN cities c ON rs.city_id = c.city_id
                WHERE rs.route_id = trip_rec.route_id
                AND rs.sequence > stop_source.sequence
                AND rs.is_dropping_point = true
                ORDER BY rs.sequence
            LOOP
                -- Calculate boarding and dropping epochs
                boarding_epoch := trip_rec.start_datetime_epoch + 
                    (COALESCE(stop_source.departure_offset_minutes, 0) * 60);
                dropping_epoch := trip_rec.start_datetime_epoch + 
                    (COALESCE(stop_dest.arrival_offset_minutes, 0) * 60);
                
                -- Calculate fare
                fare_calc := COALESCE(stop_dest.fare_from_start, 0.0) - 
                           COALESCE(stop_source.fare_from_start, 0.0);
                IF fare_calc <= 0 THEN
                    fare_calc := ABS(COALESCE(stop_source.fare_from_start, 0.0)) + 100;
                END IF;
                
                -- Insert into searchable_routes (only if not exists)
                INSERT INTO searchable_routes (
                    trip_id, is_active, route_id,
                    source_city_id, destination_city_id,
                    boarding_datetime, dropping_datetime, travel_date,
                    seat_count, seat_type, bus_id, fare, rating,
                    has_wifi, has_charging_point, has_blanket, is_ac,
                    other_attributes, created_at_epoch
                )
                SELECT
                    trip_rec.trip_id,
                    true,
                    trip_rec.route_id,
                    stop_source.city_id,
                    stop_dest.city_id,
                    boarding_epoch,
                    dropping_epoch,
                    trip_rec.date,
                    COALESCE(total_seats_var, 40),
                    COALESCE(seat_type_var, 'SLEEPER'),
                    (SELECT bus_id FROM routes WHERE route_id = trip_rec.route_id),
                    fare_calc,
                    rating_var,
                    COALESCE(has_wifi_var, false),
                    COALESCE(has_charging_var, false),
                    COALESCE(has_blanket_var, false),
                    COALESCE(is_ac_var, false),
                    other_attrs,
                    EXTRACT(EPOCH FROM NOW())::BIGINT
                WHERE NOT EXISTS (
                    SELECT 1 FROM searchable_routes
                    WHERE trip_id = trip_rec.trip_id
                    AND source_city_id = stop_source.city_id
                    AND destination_city_id = stop_dest.city_id
                );
                
                IF FOUND THEN
                    created_count := created_count + 1;
                END IF;
            END LOOP;
        END LOOP;
    END LOOP;
    
    RAISE NOTICE 'Created % searchable route records', created_count;
END $$;

