-- Migration to update searchable_routes table:
-- 1. Add source_city_code and destination_city_code columns
-- 2. Remove rating column
-- 3. Keep city_id columns for backward compatibility (can be removed later)

-- Add city_code columns
ALTER TABLE searchable_routes 
ADD COLUMN IF NOT EXISTS source_city_code VARCHAR(10),
ADD COLUMN IF NOT EXISTS destination_city_code VARCHAR(10);

-- Remove rating column
ALTER TABLE searchable_routes 
DROP COLUMN IF EXISTS rating;

-- Update indexes to include city_code
DROP INDEX IF EXISTS idx_searchable_route_source_dest_date;
CREATE INDEX idx_searchable_route_source_dest_date ON searchable_routes(source_city_code, destination_city_code, travel_date, is_active);

-- Update filter index (remove rating)
DROP INDEX IF EXISTS idx_searchable_route_filters;
CREATE INDEX idx_searchable_route_filters ON searchable_routes(is_ac, seat_type, is_active);

-- Add index on city codes for faster lookups
CREATE INDEX IF NOT EXISTS idx_searchable_route_source_city_code ON searchable_routes(source_city_code);
CREATE INDEX IF NOT EXISTS idx_searchable_route_dest_city_code ON searchable_routes(destination_city_code);

