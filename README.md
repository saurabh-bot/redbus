# 🚌 RedBus-like Bus Booking System

A production-ready microservices-based bus booking system implementing Domain-Driven Design (DDD) principles. This system enables users to search, book, and cancel bus tickets with features like seat selection, fare calculation, and real-time availability.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)
- [Potential Next Steps](#potential-next-steps)

---

## 🎯 Overview

This is a complete bus booking platform similar to RedBus, built with microservices architecture. The system handles:

- **Bus Search**: Find buses between cities with filters (AC/Non-AC, Seat Type, etc.)
- **Seat Selection**: View seat layout and select available seats
- **Booking Management**: Create, confirm, and cancel bookings
- **Real-time Availability**: Check seat availability with Redis-based locking


### Key Highlights

✅ **Microservices Architecture** - 4 independent services  
✅ **Production Ready** - Error handling, logging, validation, caching  
✅ **Database Optimized** - Proper indices, migrations, normalization  
✅ **Redis Integration** - Caching and seat locking  
✅ **Unit Tested** - 46 unit tests covering critical functionality  
✅ **Zero Manual Setup** - One-command setup with seed data

---

## 🏗️ Architecture

The system consists of 4 microservices:

### 1. **Gateway Service** (Port: 8080)
- API Gateway for all external requests
- Routes requests to appropriate microservices
- Orchestrates booking confirmation and cancellation flows

### 2. **Fleet Service** (Port: 8081)
- Manages buses, routes, cities, and route stops
- Handles seat layouts and seat reservations
- Implements seat locking with Redis (15-minute TTL)
- Manages trip instances (daily schedules)

### 3. **Search Service** (Port: 8082)
- Handles bus search queries
- Maintains denormalized `searchable_routes` table for fast searches
- Supports filtering (AC/Non-AC, Seat Type) and sorting (fare, departure)
- Redis caching for search results

### 4. **Booking Service** (Port: 8083)
- Manages bookings and booking references
- Stores passenger details
- Handles booking status transitions (INITIATED → COMPLETED → CANCELLED)

### Database Architecture

- **Fleet DB** (`fleet_db`): Cities, Buses, Routes, Route Stops, Trip Instances, Seat Layouts, Seat Reservations
- **Search DB** (`search_db`): Searchable Routes (denormalized)
- **Booking DB** (`booking_db`): Bookings, Booking Passengers, Cancellations

### Infrastructure

- **PostgreSQL**: Primary database (3 separate databases)
- **Redis**: 
  - Port 6379 - Fleet Service (seat locking)
  - Port 6380 - Search Service (cache)

---

## ✨ Features

### Bus Search
- Search buses by source city, destination city, and travel date
- Filter by AC/Non-AC and Seat Type (SEATER/SLEEPER)
- Sort by fare (low to high) or departure time
- Pagination support

### Seat Management
- View seat layout with position, type, and availability status
- Real-time seat availability (AVAILABLE/LOCKED/BOOKED)
- Seat locking mechanism (15-minute TTL) to prevent double booking
- Overlap detection for route segments

### Booking Flow
1. **Search** → Find buses for source-destination
2. **View Seats** → Check seat availability
3. **Lock Seats** → Temporarily reserve seats (15 min)
4. **Create Booking** → Enter passenger details
5. **Confirm Booking** → Complete payment and confirm seats
6. **Cancel Booking** → Cancel and release seats (only COMPLETED bookings)

### Data Management
- Automatic trip instance generation
- Searchable routes auto-population
- Seat layouts with visual positioning
- City codes (unique, auto-generated)

---

## 🛠️ Technology Stack

### Framework & Languages
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Cloud Gateway**
- **Spring Data JPA**
- **Spring Data Redis**
- **Maven** (Build tool)

### Databases & Caching
- **PostgreSQL 15** (Primary database)
- **Redis 7** (Caching & Locking)
- **Flyway** (Database migrations)

### Libraries
- **MapStruct** (Object mapping)
- **Lombok** (Boilerplate reduction)
- **Jackson** (JSON processing)
- **Resilience4j** (Circuit breaker, retry)
- **Micrometer** (Metrics)
- **SpringDoc OpenAPI** (API documentation)
- **TestContainers** (Integration testing)

### Testing
- **JUnit 5**
- **Mockito**
- **AssertJ**

---

## 📦 Prerequisites

- **macOS** (tested on macOS 13+)
- **Java 17+** - Check: `java -version`
- **Maven 3.6+** - Check: `mvn -version`
- **Homebrew** - Will be installed automatically if missing

> **Note**: No Docker required! The setup script installs PostgreSQL and Redis locally via Homebrew.

---

## 🚀 Quick Start

### One-Command Setup

```bash
# Clone or extract the project, then:
chmod +x setup.sh
./setup.sh
```

The script will:
1. ✅ Install PostgreSQL and Redis (if not present)
2. ✅ Start database services
3. ✅ Create 3 databases (fleet_db, search_db, booking_db)
4. ✅ Build all microservices
5. ✅ Start all 4 services
6. ✅ Wait for services to initialize
7. ✅ Shared database_dump also, so no need to create seed data
8. ✅ Can easily Start testing with gateway APIs or core APIs

**Expected Time**: 2-3 minutes

### Verify Services

After setup completes, verify all services are running:

```bash
curl http://localhost:8080/actuator/health  # Gateway
curl http://localhost:8081/actuator/health  # Fleet
curl http://localhost:8082/actuator/health  # Search
curl http://localhost:8083/actuator/health  # Booking
```

All should return `{"status":"UP"}`.

---

## 📁 Project Structure

```
redbus/
├── gateway-service/          # Port 8080 - API Gateway
│   ├── src/main/java/
│   │   └── com/redbus/gateway/
│   │       ├── controller/     # Gateway controllers
│   │       ├── config/         # Service configurations
│   │       └── util/           # Utilities
│   └── pom.xml
│
├── fleet-service/              # Port 8081 - Fleet Management
│   ├── src/main/java/
│   │   └── com/redbus/fleet/
│   │       ├── controller/     # REST controllers
│   │       ├── service/        # Business logic
│   │       ├── repository/    # Data access
│   │       ├── model/          # Entities
│   │       ├── dto/           # Data transfer objects
│   │       ├── exception/     # Custom exceptions
│   │       └── constants/     # Constants
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/migration/       # Flyway migrations
│   ├── src/test/java/        # Unit tests
│   └── pom.xml
│
├── search-service/            # Port 8082 - Search
│   ├── src/main/java/
│   │   └── com/redbus/search/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       └── model/
│   ├── src/main/resources/
│   │   └── db/migration/
│   ├── src/test/java/
│   └── pom.xml
│
├── booking-service/           # Port 8083 - Booking
│   ├── src/main/java/
│   │   └── com/redbus/booking/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── repository/
│   │       └── model/
│   ├── src/main/resources/
│   │   └── db/migration/
│   ├── src/test/java/
│   └── pom.xml
│
├── pom.xml                    # Parent POM
├── setup.sh                   # Complete setup script
├── README.md                  # This file
└── RedBus_API_Collection.postman_collection.json  # Postman collection
```

---

## 📚 API Documentation

### Swagger UI

Once services are running, access Swagger UI:

- **Fleet Service**: http://localhost:8081/swagger-ui.html
- **Search Service**: http://localhost:8082/swagger-ui.html
- **Booking Service**: http://localhost:8083/swagger-ui.html

### Postman Collection

Import `RedBus_API_Collection.postman_collection.json` into Postman for pre-configured API requests.


### Seed Data (Database dump)

The setup script automatically creates:

- **Cities**: 6 cities (Delhi, Ajmer, Jaipur, Kanpur, Lucknow, Ayodhya)
- **Buses**: 3 buses (AC Sleeper, Non-AC Seater, AC Seater)
- **Routes**: 3 routes with different stop configurations
- **Trips**: Trip instances for November 5-15, 2025 across all routes
- **Seat Layouts**: 40 seats per bus (120 total seats) with visual positioning
- **Searchable Routes**: All source-destination combinations populated and ready for searching

---

## 🧪 Testing

### Manual workflow testing

https://docs.google.com/document/d/1aN1dhNoV0vI5zhGIJBRc_w-TQkkRZOjyZy_MEmLWtJg/edit?usp=sharing

### Unit Tests

Run all unit tests:

```bash
# From root directory
mvn test

# Or for a specific service
cd fleet-service && mvn test
```

**Test Coverage**:
- **Fleet Service**: 21 tests (Seat Availability, Locking, Reservations)
- **Search Service**: 11 tests (Search scenarios, filtering, sorting)
- **Booking Service**: 14 tests (Booking creation, confirmation, cancellation)

**Out of Scope for Unit Tests**:
- **User Service**: Authentication, authorization, and user profile management are not included in the current test suite
- **Payment Service**: Payment processing and transaction handling tests are deferred as the payment service is a placeholder implementation

```

---

## 🔧 Troubleshooting

### Services Not Starting

1. **Check ports are free**:
   ```bash
   lsof -i :8080  # Gateway
   lsof -i :8081  # Fleet
   lsof -i :8082  # Search
   lsof -i :8083  # Booking
   ```

2. **Check database connectivity**:
   ```bash
   psql -l  # Should list fleet_db, search_db, booking_db
   ```

3. **Check Redis**:
   ```bash
   redis-cli ping  # Should return PONG
   redis-cli -p 6380 ping  # Should return PONG
   ```

### Database Connection Issues

1. **Verify PostgreSQL is running**:
   ```bash
   brew services list | grep postgresql
   ```

2. **Check database exists**:
   ```bash
   psql -l | grep -E "fleet_db|search_db|booking_db"
   ```

3. **Verify credentials**: Check `application.properties` in each service.

### Redis Connection Issues

1. **Verify Redis instances**:
   ```bash
   redis-cli ping          # Port 6379 (Fleet)
   redis-cli -p 6380 ping  # Port 6380 (Search)
   ```

2. **Check Redis processes**:
   ```bash
   ps aux | grep redis
   ```

### Logs

Check service logs in the project root:
- `fleet-service.log`
- `search-service.log`
- `booking-service.log`
- `gateway-service.log`

---

## 📊 Service Ports Summary

| Service | Port | Health Check | Swagger UI |
|---------|------|--------------|------------|
| Gateway | 8080 | http://localhost:8080/actuator/health | N/A |
| Fleet | 8081 | http://localhost:8081/actuator/health | http://localhost:8081/swagger-ui.html |
| Search | 8082 | http://localhost:8082/actuator/health | http://localhost:8082/swagger-ui.html |
| Booking | 8083 | http://localhost:8083/actuator/health | http://localhost:8083/swagger-ui.html |

---

## 🎓 Key Design Decisions

### Microservices Architecture
- **Separation of Concerns**: Each service handles a specific domain
- **Independent Scaling**: Services can scale independently based on load
- **Technology Flexibility**: Each service can evolve independently

### Database Design
- **Separate Databases**: Each service has its own database
- **Denormalization**: Search service uses denormalized `searchable_routes` for fast queries
- **Strong Consistency**: Booking and Fleet services maintain ACID guarantees

### Caching Strategy
- **Search Results**: Cached in Redis (15-minute TTL)
- **Seat Locks**: Stored in Redis with 15-minute expiration
- **Cache Invalidation**: On seat booking/cancellation

### Seat Locking
- **Distributed Locking**: Redis-based locks prevent double booking
- **TTL**: 15-minute lock expiration
- **Overlap Detection**: Prevents booking overlapping route segments

---

## 📝 Notes

- All timestamps use Unix epoch (seconds)
- City codes are auto-generated (3-5 uppercase letters)
- Booking references follow format: `REDBUS{YYYYMMDD}{6CHARS}`
- Seat numbers follow pattern: `{ROW_LETTER}{COLUMN_NUMBER}` (e.g., A1, B2)
- API requests/responses use snake_case for JSON fields

---

## 🚧 Future Enhancements

The following features can be added incrementally to extend the system's capabilities:

### Event-Driven Architecture
- **Kafka Integration**: Set up Kafka for asynchronous communication between services. This will enable trip instance and searchable route generation through event-driven workflows, making the system more scalable and decoupled.

- **Event Publishing**: Have the booking service publish domain events (like `BookingConfirmed`, `BookingCancelled`) to Kafka topics. Other services can subscribe to these events for their own processing needs.

- **Real-time Updates**: Use Kafka messages to update `seat_count` in `searchable_routes` whenever a booking is confirmed or cancelled, for filtering out bus if available_seat_count becomes 0.

### API Improvements
- **Idempotency**: Add idempotency keys to critical APIs (booking creation, confirmation) to handle retries gracefully and prevent duplicate operations.

- **Idempotency**: For confirm and cancel api, if they fail because of 5xx, whole gateway reuqest will be moved to Kafka queue so that can be retried later and all api should be idempotent so that api which ran earlier shouldn't execute again.

- **Trip References**: Replace `trip_id` in API responses with a unique `trip_reference` or `trip_name` instead of exposing database primary keys, improving security and API usability.

### Testing & Quality
- **Integration Tests**: Add comprehensive integration test suites that test the full booking flow across services, ensuring end-to-end reliability.

- **Automated Data Generation**: Implement scheduled jobs or event handlers to automatically generate trip instances and populate searchable routes, reducing manual setup overhead.

These enhancements are designed to be implemented incrementally - each one can be added independently without disrupting existing functionality. The current system is fully functional and production-ready for the core booking flow.


Note: Not able to use docker because system is not allowing installation of docker because of security issue

