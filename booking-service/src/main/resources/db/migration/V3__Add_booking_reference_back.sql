-- Add booking_reference back to bookings table
ALTER TABLE bookings ADD COLUMN IF NOT EXISTS booking_reference VARCHAR(50) UNIQUE;

-- Create unique index on booking_reference
CREATE UNIQUE INDEX IF NOT EXISTS idx_bookings_reference ON bookings(booking_reference);

-- Update status CHECK constraint to reflect new statuses (INITIATED, COMPLETED, CANCELLED, FAILED)
ALTER TABLE bookings DROP CONSTRAINT IF EXISTS bookings_status_check;
ALTER TABLE bookings ADD CONSTRAINT bookings_status_check CHECK (status IN ('INITIATED', 'COMPLETED', 'CANCELLED', 'FAILED'));
