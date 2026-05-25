DROP TABLE IF EXISTS reminder_rule;
DROP TABLE IF EXISTS mileage_record;
DROP TABLE IF EXISTS fuel_record;
DROP TABLE IF EXISTS service_record;
DROP TABLE IF EXISTS service_type;
DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS app_user;

CREATE TABLE app_user (
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(120) NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    phone         VARCHAR(30),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_app_user_email CHECK (LOCATE('@', email) > 1)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE car (
    car_id           INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT NOT NULL,
    brand            VARCHAR(60) NOT NULL,
    model            VARCHAR(80) NOT NULL,
    manufacture_year SMALLINT NOT NULL,
    vin              VARCHAR(17) UNIQUE,
    plate_number     VARCHAR(20),
    initial_mileage  INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_car_user FOREIGN KEY (user_id) REFERENCES app_user(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_car_year CHECK (manufacture_year BETWEEN 1950 AND 2030),
    CONSTRAINT chk_car_initial_mileage CHECK (initial_mileage >= 0),
    CONSTRAINT chk_car_vin CHECK (vin IS NULL OR CHAR_LENGTH(vin) = 17)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_type (
    service_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name       VARCHAR(80) NOT NULL UNIQUE,
    default_interval_km INT NOT NULL DEFAULT 7500,
    default_interval_months INT NOT NULL DEFAULT 12,
    CONSTRAINT chk_service_type_km CHECK (default_interval_km > 0),
    CONSTRAINT chk_service_type_months CHECK (default_interval_months > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE service_record (
    service_record_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id            INT NOT NULL,
    service_type_id   INT NOT NULL,
    service_date      DATE NOT NULL,
    mileage           INT NOT NULL,
    cost              DECIMAL(12,2) NOT NULL,
    comment_text      VARCHAR(500),
    CONSTRAINT fk_service_car FOREIGN KEY (car_id) REFERENCES car(car_id) ON DELETE CASCADE,
    CONSTRAINT fk_service_type FOREIGN KEY (service_type_id) REFERENCES service_type(service_type_id),
    CONSTRAINT chk_service_mileage CHECK (mileage >= 0),
    CONSTRAINT chk_service_cost CHECK (cost >= 0),
    CONSTRAINT chk_service_date CHECK (service_date BETWEEN '2000-01-01' AND '2030-12-31')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE fuel_record (
    fuel_record_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id         INT NOT NULL,
    fuel_date      DATE NOT NULL,
    mileage        INT NOT NULL,
    liters         DECIMAL(8,2) NOT NULL,
    price_per_liter DECIMAL(8,2) NOT NULL,
    station_name   VARCHAR(120),
    CONSTRAINT fk_fuel_car FOREIGN KEY (car_id) REFERENCES car(car_id) ON DELETE CASCADE,
    CONSTRAINT chk_fuel_date CHECK (fuel_date BETWEEN '2000-01-01' AND '2030-12-31'),
    CONSTRAINT chk_fuel_mileage CHECK (mileage >= 0),
    CONSTRAINT chk_fuel_liters CHECK (liters > 0),
    CONSTRAINT chk_fuel_price CHECK (price_per_liter >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE mileage_record (
    mileage_record_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id            INT NOT NULL,
    record_date       DATE NOT NULL,
    mileage           INT NOT NULL,
    note              VARCHAR(300),
    CONSTRAINT fk_mileage_car FOREIGN KEY (car_id) REFERENCES car(car_id) ON DELETE CASCADE,
    CONSTRAINT uq_mileage_car_date UNIQUE (car_id, record_date),
    CONSTRAINT chk_mileage_date CHECK (record_date BETWEEN '2000-01-01' AND '2030-12-31'),
    CONSTRAINT chk_mileage_value CHECK (mileage >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE reminder_rule (
    reminder_rule_id INT AUTO_INCREMENT PRIMARY KEY,
    car_id           INT NOT NULL,
    service_type_id  INT NOT NULL,
    interval_km      INT NOT NULL,
    interval_months  INT NOT NULL,
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_reminder_car FOREIGN KEY (car_id) REFERENCES car(car_id) ON DELETE CASCADE,
    CONSTRAINT fk_reminder_type FOREIGN KEY (service_type_id) REFERENCES service_type(service_type_id),
    CONSTRAINT uq_reminder_car_type UNIQUE (car_id, service_type_id),
    CONSTRAINT chk_reminder_km CHECK (interval_km > 0),
    CONSTRAINT chk_reminder_months CHECK (interval_months > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_car_user ON car(user_id);
CREATE INDEX idx_service_car_date ON service_record(car_id, service_date);
CREATE INDEX idx_fuel_car_date ON fuel_record(car_id, fuel_date);
CREATE INDEX idx_mileage_car_date ON mileage_record(car_id, record_date);
