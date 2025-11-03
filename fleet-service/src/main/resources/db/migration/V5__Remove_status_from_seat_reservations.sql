-- Remove status column from seat_reservations table
-- If a record exists = BOOKED, if no record = AVAILABLE
-- Status is no longer needed as LOCKED state exists only in Redis

-- Drop the status index first
DROP INDEX IF EXISTS idx_seat_reservations_status;

-- Drop the status column
ALTER TABLE seat_reservations DROP COLUMN IF EXISTS status;
