-- Rename seat_bookings table to seat_reservations to reflect that it's now agnostic of booking_id
-- The table represents seat reservations/occupancies, not bookings

-- Rename the table
ALTER TABLE seat_bookings RENAME TO seat_reservations;

-- Rename indexes to reflect new table name
ALTER INDEX idx_seat_bookings_trip_id RENAME TO idx_seat_reservations_trip_id;
ALTER INDEX idx_seat_bookings_trip_seat RENAME TO idx_seat_reservations_trip_seat;
ALTER INDEX idx_seat_bookings_status RENAME TO idx_seat_reservations_status;
ALTER INDEX idx_seat_bookings_stops RENAME TO idx_seat_reservations_stops;

-- Rename primary key constraint (if exists)
ALTER TABLE seat_reservations RENAME CONSTRAINT seat_bookings_pkey TO seat_reservations_pkey;

-- Rename foreign key constraint
ALTER TABLE seat_reservations RENAME CONSTRAINT fk_seat_bookings_trip TO fk_seat_reservations_trip;

-- Rename check constraint
ALTER TABLE seat_reservations RENAME CONSTRAINT chk_seat_bookings_sequence TO chk_seat_reservations_sequence;

-- Rename primary key column if needed (optional, but cleaner)
ALTER TABLE seat_reservations RENAME COLUMN seat_booking_id TO seat_reservation_id;
