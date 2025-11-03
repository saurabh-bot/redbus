-- Add status column to seat_reservations table
-- Status values: CREATED (active reservation), CANCELLED (cancelled reservation)
-- Only CREATED reservations should be considered for seat availability

ALTER TABLE seat_reservations ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'CREATED' 
    CHECK (status IN ('CREATED', 'CANCELLED'));

-- Create index on status for better query performance
CREATE INDEX IF NOT EXISTS idx_seat_reservations_status ON seat_reservations(status);

-- Update existing records to CREATED status
UPDATE seat_reservations SET status = 'CREATED' WHERE status IS NULL OR status = '';

