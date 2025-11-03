-- Booking Service Database Schema

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    booking_id BIGSERIAL PRIMARY KEY,
    booking_reference VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    trip_id BIGINT NOT NULL,
    payment_reference_id VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED' CHECK (status IN ('CREATED', 'LOCKED', 'CONFIRMED', 'CANCELLED', 'FAILED')),
    other_details JSONB,
    base_fare DOUBLE PRECISION,
    taxes DOUBLE PRECISION,
    convenience_fee DOUBLE PRECISION,
    total_amount DOUBLE PRECISION,
    seat_count INTEGER,
    expires_at_epoch BIGINT,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT
);

CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_trip_id ON bookings(trip_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE UNIQUE INDEX idx_bookings_reference ON bookings(booking_reference);

-- Booking Passengers Table
CREATE TABLE IF NOT EXISTS booking_passengers (
    passenger_id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone_number VARCHAR(20),
    gender VARCHAR(10),
    age INTEGER,
    seat_number VARCHAR(10) NOT NULL,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_booking_passengers_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    CONSTRAINT uk_booking_passengers_seat UNIQUE (booking_id, seat_number)
);

CREATE INDEX idx_booking_passengers_booking_id ON booking_passengers(booking_id);
CREATE INDEX idx_booking_passengers_seat ON booking_passengers(booking_id, seat_number);

-- Cancellations Table
CREATE TABLE IF NOT EXISTS cancellations (
    cancellation_id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reason VARCHAR(500),
    refund_amount DOUBLE PRECISION,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PROCESSED', 'FAILED')),
    cancelled_seats JSONB,
    created_at_epoch BIGINT NOT NULL,
    updated_at_epoch BIGINT,
    CONSTRAINT fk_cancellations_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

CREATE INDEX idx_cancellations_booking_id ON cancellations(booking_id);
CREATE INDEX idx_cancellations_user_id ON cancellations(user_id);
CREATE INDEX idx_cancellations_status ON cancellations(status);
