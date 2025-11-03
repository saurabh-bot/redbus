-- Remove booking_id column from seat_bookings table to enforce strict service boundaries
-- Fleet Service should not know about bookings, only about seat availability/booking

-- Drop the index first
DROP INDEX IF EXISTS idx_seat_bookings_booking_id;

-- Drop the column
ALTER TABLE seat_bookings DROP COLUMN IF EXISTS booking_id;
