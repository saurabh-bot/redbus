-- Simplify bookings table structure
-- Remove: booking_reference, trip_id, other_details, base_fare, taxes, convenience_fee, seat_count, expires_at_epoch
-- Add: trip_details JSONB field to store all trip-related information

-- Drop indexes that reference columns we're removing
DROP INDEX IF EXISTS idx_bookings_reference;
DROP INDEX IF EXISTS idx_bookings_trip_id;

-- Add new trip_details column
ALTER TABLE bookings ADD COLUMN IF NOT EXISTS trip_details JSONB;

-- Remove columns
ALTER TABLE bookings DROP COLUMN IF EXISTS booking_reference;
ALTER TABLE bookings DROP COLUMN IF EXISTS trip_id;
ALTER TABLE bookings DROP COLUMN IF EXISTS other_details;
ALTER TABLE bookings DROP COLUMN IF EXISTS base_fare;
ALTER TABLE bookings DROP COLUMN IF EXISTS taxes;
ALTER TABLE bookings DROP COLUMN IF EXISTS convenience_fee;
ALTER TABLE bookings DROP COLUMN IF EXISTS seat_count;
ALTER TABLE bookings DROP COLUMN IF EXISTS expires_at_epoch;
