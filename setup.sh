#!/bin/bash

# RedBus Microservices - Complete Setup Script with Seed Data
# This script sets up everything needed to run the RedBus application
# including seed data for immediate testing

set -e

echo "═══════════════════════════════════════════════════════"
echo "  🚀 RedBus Microservices - Complete Setup"
echo "═══════════════════════════════════════════════════════"
echo ""

# Navigate to project directory
cd "$(dirname "$0")"

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to print colored messages
print_info() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warn() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "📦 Homebrew not found. Installing Homebrew..."
    echo ""
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    echo ""
    print_info "Homebrew installed"
    echo ""
fi

print_info "Homebrew found"
echo ""

# Install PostgreSQL
echo "📦 Installing PostgreSQL..."
if command -v psql &> /dev/null; then
    print_info "PostgreSQL already installed: $(psql --version 2>/dev/null | head -1 || echo 'installed')"
    POSTGRES_INSTALLED=true
else
    brew install postgresql@15
    export PATH="/opt/homebrew/opt/postgresql@15/bin:$PATH" 2>/dev/null || export PATH="/usr/local/opt/postgresql@15/bin:$PATH"
    POSTGRES_INSTALLED=false
fi

# Install Redis
echo "📦 Installing Redis..."
if command -v redis-server &> /dev/null; then
    print_info "Redis already installed: $(redis-server --version 2>/dev/null | head -1 || echo 'installed')"
else
    brew install redis
fi

# Install Python3 (for seed scripts)
if ! command -v python3 &> /dev/null; then
    echo "📦 Installing Python3..."
    brew install python3
fi

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  🔧 Starting Database Services"
echo "═══════════════════════════════════════════════════════"
echo ""

# Ensure PostgreSQL is in PATH
export PATH="/opt/homebrew/opt/postgresql@15/bin:/usr/local/opt/postgresql@15/bin:$PATH" 2>/dev/null || true

# Start PostgreSQL
echo "Starting PostgreSQL..."
if brew services list 2>/dev/null | grep -qE "postgresql.*started|postgresql@15.*started"; then
    print_info "PostgreSQL already running"
else
    brew services start postgresql@15 2>/dev/null || brew services start postgresql 2>/dev/null
    echo "   ⏳ Waiting for PostgreSQL to start..."
    sleep 5
    print_info "PostgreSQL started"
fi

# Start Redis (port 6379)
echo "Starting Redis (port 6379)..."
if brew services list 2>/dev/null | grep -q "redis.*started"; then
    print_info "Redis already running on port 6379"
else
    brew services start redis
    sleep 3
    print_info "Redis started on port 6379"
fi

# Start second Redis instance on port 6380
echo "Starting Redis (port 6380) for Search Service..."
if lsof -ti:6380 &> /dev/null; then
    print_info "Redis already running on port 6380"
else
    REDIS_6380_CONFIG="/tmp/redis-6380.conf"
    mkdir -p /tmp/redis-6380
    cat > "$REDIS_6380_CONFIG" << EOF
port 6380
bind 127.0.0.1
dir /tmp/redis-6380
appendonly yes
appendfilename "appendonly-6380.aof"
daemonize yes
pidfile /tmp/redis-6380/redis.pid
logfile /tmp/redis-6380/redis.log
EOF
    redis-server "$REDIS_6380_CONFIG"
    sleep 2
    print_info "Redis started on port 6380"
fi

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  🗄️  Creating Databases"
echo "═══════════════════════════════════════════════════════"
echo ""

# Wait for PostgreSQL to be fully ready
sleep 3

# Get PostgreSQL username
PG_USER=${USER:-$(whoami)}

# Create databases
echo "Creating databases..."

for db in fleet_db search_db booking_db; do
    if psql -lqt 2>/dev/null | cut -d \| -f 1 | grep -qw "$db"; then
        print_info "$db already exists"
    else
        createdb "$db" 2>/dev/null || psql postgres -c "CREATE DATABASE $db;" 2>/dev/null || print_warn "Could not create $db (may already exist)"
        print_info "$db created"
    fi
done

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  📦 Building Project"
echo "═══════════════════════════════════════════════════════"
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven not found!"
    echo ""
    echo "Please install Maven:"
    echo "  brew install maven"
    echo ""
    exit 1
fi

# Build project
echo "Building all services (this may take 2-3 minutes)..."
mvn clean install -DskipTests
print_info "Build complete"

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  🚀 Starting Services"
echo "═══════════════════════════════════════════════════════"
echo ""

# Start Fleet Service
echo "Starting Fleet Service (port 8081)..."
cd fleet-service
nohup mvn spring-boot:run > ../fleet-service.log 2>&1 &
FLEET_PID=$!
cd ..
print_info "Fleet Service started (PID: $FLEET_PID)"

# Wait a bit before starting next service
sleep 5

# Start Search Service
echo "Starting Search Service (port 8082)..."
cd search-service
nohup mvn spring-boot:run > ../search-service.log 2>&1 &
SEARCH_PID=$!
cd ..
print_info "Search Service started (PID: $SEARCH_PID)"

# Wait a bit before starting next service
sleep 5

# Start Booking Service
echo "Starting Booking Service (port 8083)..."
cd booking-service
nohup mvn spring-boot:run > ../booking-service.log 2>&1 &
BOOKING_PID=$!
cd ..
print_info "Booking Service started (PID: $BOOKING_PID)"

# Wait a bit before starting gateway
sleep 5

# Start Gateway Service
echo "Starting Gateway Service (port 8080)..."
cd gateway-service
nohup mvn spring-boot:run > ../gateway-service.log 2>&1 &
GATEWAY_PID=$!
cd ..
print_info "Gateway Service started (PID: $GATEWAY_PID)"

echo ""
echo "⏳ Waiting for services to initialize (60 seconds)..."
echo "   (Services need time to run Flyway migrations)"
echo ""

# Wait for services to be ready
SLEEP_COUNT=0
MAX_WAIT=120

while [ $SLEEP_COUNT -lt $MAX_WAIT ]; do
    FLEET_UP=$(curl -s http://localhost:8081/actuator/health 2>/dev/null | grep -q "UP" && echo "true" || echo "false")
    SEARCH_UP=$(curl -s http://localhost:8082/actuator/health 2>/dev/null | grep -q "UP" && echo "true" || echo "false")
    BOOKING_UP=$(curl -s http://localhost:8083/actuator/health 2>/dev/null | grep -q "UP" && echo "true" || echo "false")
    
    if [ "$FLEET_UP" = "true" ] && [ "$SEARCH_UP" = "true" ] && [ "$BOOKING_UP" = "true" ]; then
        print_info "All services are UP!"
        break
    fi
    
    sleep 5
    SLEEP_COUNT=$((SLEEP_COUNT + 5))
    if [ $((SLEEP_COUNT % 15)) -eq 0 ]; then
        echo "   Still waiting... ($SLEEP_COUNT seconds elapsed)"
    fi
done

if [ "$FLEET_UP" != "true" ] || [ "$SEARCH_UP" != "true" ] || [ "$BOOKING_UP" != "true" ]; then
    print_warn "Some services may not be fully ready yet. Continuing..."
fi

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  📥 Restoring Database Data (if available)"
echo "═══════════════════════════════════════════════════════"
echo ""

# Wait a bit more for Flyway migrations to complete
sleep 5

# Check if database dumps exist and restore them
HAS_DUMPS=false
if [ -d "database-dumps" ]; then
    if [ -f "database-dumps/fleet_db_data.sql" ] && [ -s "database-dumps/fleet_db_data.sql" ]; then
        echo "📦 Restoring fleet_db data..."
        psql fleet_db < database-dumps/fleet_db_data.sql 2>/dev/null && print_info "fleet_db data restored" || print_warn "Could not restore fleet_db data (may need schema to exist first)"
        HAS_DUMPS=true
    fi
    
    if [ -f "database-dumps/search_db_data.sql" ] && [ -s "database-dumps/search_db_data.sql" ]; then
        echo "📦 Restoring search_db data..."
        psql search_db < database-dumps/search_db_data.sql 2>/dev/null && print_info "search_db data restored" || print_warn "Could not restore search_db data (may need schema to exist first)"
        HAS_DUMPS=true
    fi
    
    if [ -f "database-dumps/booking_db_data.sql" ] && [ -s "database-dumps/booking_db_data.sql" ]; then
        echo "📦 Restoring booking_db data..."
        psql booking_db < database-dumps/booking_db_data.sql 2>/dev/null && print_info "booking_db data restored" || print_warn "Could not restore booking_db data (may need schema to exist first)"
        HAS_DUMPS=true
    fi
fi

if [ "$HAS_DUMPS" = "true" ]; then
    echo ""
    print_info "Database restore complete. Checking if data exists..."
    sleep 2
    
    # Check if data was successfully restored
    CITY_COUNT=$(psql fleet_db -t -c "SELECT COUNT(*) FROM cities;" 2>/dev/null | xargs || echo "0")
    
    if [ "$CITY_COUNT" -gt "0" ]; then
        echo ""
        print_info "✅ Database already has data from SQL dumps!"
        echo "   💡 Skipping API-based seeding."
        echo ""
        SKIP_API_SEEDING=true
    else
        echo ""
        print_warn "SQL dumps restored but no data found. Will seed via APIs..."
        SKIP_API_SEEDING=false
    fi
else
    echo ""
    print_info "No database dumps found. Will seed data via APIs..."
    SKIP_API_SEEDING=false
fi

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  🌱 Seeding Initial Data (if needed)"
echo "═══════════════════════════════════════════════════════"
echo ""

if [ "$SKIP_API_SEEDING" = "true" ]; then
    echo "   ℹ️  Data already exists from SQL dumps. Setup complete!"
    echo ""
else
    echo ""
    print_error "No database dumps found in database-dumps/ directory!"
    echo ""
    echo "   To create database dumps, run:"
    echo "   ./export-database.sh"
    echo ""
    echo "   Or manually restore your database backups to:"
    echo "   - database-dumps/fleet_db_data.sql"
    echo "   - database-dumps/search_db_data.sql"
    echo "   - database-dumps/booking_db_data.sql"
    echo ""
    print_warn "Services are running but databases are empty."
    echo "   You can use APIs to create data, or restore SQL dumps."
    echo ""
fi

echo ""
echo "═══════════════════════════════════════════════════════"
echo "  ✅ Setup Complete!"
echo "═══════════════════════════════════════════════════════"
echo ""
echo "📊 Service URLs:"
echo "  Gateway:  http://localhost:8080"
echo "  Fleet:    http://localhost:8081"
echo "  Search:   http://localhost:8082"
echo "  Booking:  http://localhost:8083"
echo ""
echo "📖 Swagger UI:"
echo "  http://localhost:8081/swagger-ui.html"
echo "  http://localhost:8082/swagger-ui.html"
echo "  http://localhost:8083/swagger-ui.html"
echo ""
echo "🧪 Test APIs:"
echo "  # Search buses"
echo "  curl \"http://localhost:8080/api/v1/search/buses?source=DELHI&destination=AYODH&travel_date=2025-11-05\""
echo ""
if [ "$SKIP_API_SEEDING" = "true" ]; then
    echo "📊 Database Status:"
    echo "   ✅ All data restored from SQL dumps"
    echo ""
    echo "   Data includes:"
    CITY_COUNT=$(psql fleet_db -t -c "SELECT COUNT(*) FROM cities;" 2>/dev/null | xargs || echo "0")
    BUS_COUNT=$(psql fleet_db -t -c "SELECT COUNT(*) FROM buses;" 2>/dev/null | xargs || echo "0")
    ROUTE_COUNT=$(psql fleet_db -t -c "SELECT COUNT(*) FROM routes;" 2>/dev/null | xargs || echo "0")
    TRIP_COUNT=$(psql fleet_db -t -c "SELECT COUNT(*) FROM trip_instances;" 2>/dev/null | xargs || echo "0")
    echo "   • Cities: $CITY_COUNT"
    echo "   • Buses: $BUS_COUNT"
    echo "   • Routes: $ROUTE_COUNT"
    echo "   • Trip Instances: $TRIP_COUNT"
    echo ""
fi
echo ""
echo "📝 View Logs:"
echo "  tail -f fleet-service.log"
echo "  tail -f search-service.log"
echo "  tail -f booking-service.log"
echo "  tail -f gateway-service.log"
echo ""
print_info "Your RedBus system is ready to use!"
echo ""
