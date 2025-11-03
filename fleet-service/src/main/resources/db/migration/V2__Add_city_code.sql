-- Add city_code column to cities table
ALTER TABLE cities ADD COLUMN IF NOT EXISTS city_code VARCHAR(10);

-- Generate city codes for existing cities
-- Using a simple algorithm: first 3-5 uppercase letters of city name
UPDATE cities 
SET city_code = UPPER(
    CASE 
        WHEN LENGTH(REGEXP_REPLACE(city_name, '[^A-Za-z]', '', 'g')) <= 5 
        THEN LEFT(REGEXP_REPLACE(city_name, '[^A-Za-z]', '', 'g'), 5)
        ELSE LEFT(REGEXP_REPLACE(city_name, '[^A-Za-z]', '', 'g'), 3)
    END
)
WHERE city_code IS NULL OR city_code = '';

-- Handle duplicates by appending numbers
DO $$
DECLARE
    city_rec RECORD;
    new_code VARCHAR(10);
    counter INTEGER;
BEGIN
    FOR city_rec IN 
        SELECT city_id, city_name, city_code 
        FROM cities 
        WHERE city_code IN (
            SELECT city_code 
            FROM cities 
            GROUP BY city_code 
            HAVING COUNT(*) > 1
        )
        ORDER BY city_id
    LOOP
        counter := 1;
        new_code := city_rec.city_code;
        
        WHILE EXISTS (SELECT 1 FROM cities WHERE city_code = new_code AND city_id != city_rec.city_id) LOOP
            new_code := LEFT(city_rec.city_code, LEAST(LENGTH(city_rec.city_code), 4)) || CAST(counter AS VARCHAR);
            counter := counter + 1;
            
            IF counter > 999 THEN
                RAISE EXCEPTION 'Unable to generate unique city code for city_id: %', city_rec.city_id;
            END IF;
        END LOOP;
        
        UPDATE cities SET city_code = new_code WHERE city_id = city_rec.city_id;
    END LOOP;
END $$;

-- Add NOT NULL constraint and unique index
ALTER TABLE cities ALTER COLUMN city_code SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_cities_code ON cities(city_code);

