-- Remove unused indices from Booking Service
-- Analysis: trip_id index is not used in any queries

-- Remove idx_bookings_trip_id (not used in any repository queries)
DROP INDEX IF EXISTS idx_bookings_trip_id;

