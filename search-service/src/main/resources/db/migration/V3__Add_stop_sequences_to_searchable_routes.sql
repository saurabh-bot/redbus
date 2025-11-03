-- Add from_stop_sequence and to_stop_sequence columns to searchable_routes table
ALTER TABLE searchable_routes 
ADD COLUMN IF NOT EXISTS from_stop_sequence INTEGER,
ADD COLUMN IF NOT EXISTS to_stop_sequence INTEGER;

-- Create index on stop sequences for faster lookups
CREATE INDEX IF NOT EXISTS idx_searchable_route_stop_sequences ON searchable_routes(from_stop_sequence, to_stop_sequence);
