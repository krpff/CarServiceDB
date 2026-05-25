DROP TABLE IF EXISTS reminder_rule CASCADE;
DROP TABLE IF EXISTS mileage_record CASCADE;
DROP TABLE IF EXISTS fuel_record CASCADE;
DROP TABLE IF EXISTS service_record CASCADE;
DROP TABLE IF EXISTS service_type CASCADE;
DROP TABLE IF EXISTS car CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;

CREATE TABLE app_user (
    user_id       SERIAL PRIMARY KEY,
    full_name     VARCHAR(120) NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    phone         VARCHAR(30),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_app_user_email CHECK (position('@' in email) > 1)
);

CREATE TABLE car (
    car_id           SERIAL PRIMARY KEY,
    user_id          INTEGER NOT NULL REFERENCES app_user(user_id) ON DELETE CASCADE,
    brand            VARCHAR(60) NOT NULL,
    model            VARCHAR(80) NOT NULL,
    manufacture_year SMALLINT NOT NULL,
    vin              VARCHAR(17) UNIQUE,
    plate_number     VARCHAR(20),
    initial_mileage  INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT chk_car_year CHECK (manufacture_year BETWEEN 1950 AND EXTRACT(YEAR FROM CURRENT_DATE)::INT + 1),
    CONSTRAINT chk_car_initial_mileage CHECK (initial_mileage >= 0),
    CONSTRAINT chk_car_vin CHECK (vin IS NULL OR char_length(vin) = 17)
);

CREATE TABLE service_type (
    service_type_id SERIAL PRIMARY KEY,
    type_name       VARCHAR(80) NOT NULL UNIQUE,
    default_interval_km INTEGER NOT NULL DEFAULT 7500,
    default_interval_months INTEGER NOT NULL DEFAULT 12,
    CONSTRAINT chk_service_type_km CHECK (default_interval_km > 0),
    CONSTRAINT chk_service_type_months CHECK (default_interval_months > 0)
);

CREATE TABLE service_record (
    service_record_id SERIAL PRIMARY KEY,
    car_id            INTEGER NOT NULL REFERENCES car(car_id) ON DELETE CASCADE,
    service_type_id   INTEGER NOT NULL REFERENCES service_type(service_type_id),
    service_date      DATE NOT NULL,
    mileage           INTEGER NOT NULL,
    cost              NUMERIC(12,2) NOT NULL,
    comment_text      VARCHAR(500),
    CONSTRAINT chk_service_mileage CHECK (mileage >= 0),
    CONSTRAINT chk_service_cost CHECK (cost >= 0),
    CONSTRAINT chk_service_date CHECK (service_date <= CURRENT_DATE)
);

CREATE TABLE fuel_record (
    fuel_record_id SERIAL PRIMARY KEY,
    car_id         INTEGER NOT NULL REFERENCES car(car_id) ON DELETE CASCADE,
    fuel_date      DATE NOT NULL,
    mileage        INTEGER NOT NULL,
    liters         NUMERIC(8,2) NOT NULL,
    price_per_liter NUMERIC(8,2) NOT NULL,
    station_name   VARCHAR(120),
    CONSTRAINT chk_fuel_date CHECK (fuel_date <= CURRENT_DATE),
    CONSTRAINT chk_fuel_mileage CHECK (mileage >= 0),
    CONSTRAINT chk_fuel_liters CHECK (liters > 0),
    CONSTRAINT chk_fuel_price CHECK (price_per_liter >= 0)
);

CREATE TABLE mileage_record (
    mileage_record_id SERIAL PRIMARY KEY,
    car_id            INTEGER NOT NULL REFERENCES car(car_id) ON DELETE CASCADE,
    record_date       DATE NOT NULL,
    mileage           INTEGER NOT NULL,
    note              VARCHAR(300),
    CONSTRAINT uq_mileage_car_date UNIQUE (car_id, record_date),
    CONSTRAINT chk_mileage_date CHECK (record_date <= CURRENT_DATE),
    CONSTRAINT chk_mileage_value CHECK (mileage >= 0)
);

CREATE TABLE reminder_rule (
    reminder_rule_id SERIAL PRIMARY KEY,
    car_id           INTEGER NOT NULL REFERENCES car(car_id) ON DELETE CASCADE,
    service_type_id  INTEGER NOT NULL REFERENCES service_type(service_type_id),
    interval_km      INTEGER NOT NULL,
    interval_months  INTEGER NOT NULL,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_reminder_car_type UNIQUE (car_id, service_type_id),
    CONSTRAINT chk_reminder_km CHECK (interval_km > 0),
    CONSTRAINT chk_reminder_months CHECK (interval_months > 0)
);

CREATE INDEX idx_car_user ON car(user_id);
CREATE INDEX idx_service_car_date ON service_record(car_id, service_date);
CREATE INDEX idx_fuel_car_date ON fuel_record(car_id, fuel_date);
CREATE INDEX idx_mileage_car_date ON mileage_record(car_id, record_date);
