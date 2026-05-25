-- Эти запросы специально должны завершиться ошибкой: так проверяется работа ограничений.
-- 1. Нельзя добавить пользователя с email без символа @
INSERT INTO app_user(full_name, email) VALUES ('Ошибка Email', 'wrong-email');

-- 2. Нельзя добавить автомобиль с отрицательным пробегом
INSERT INTO car(user_id, brand, model, manufacture_year, initial_mileage)
VALUES (1, 'Test', 'BadMileage', 2020, -1);

-- 3. Нельзя добавить сервис с отрицательной стоимостью
INSERT INTO service_record(car_id, service_type_id, service_date, mileage, cost)
VALUES (1, 1, CURRENT_DATE, 90000, -100.00);

-- 4. Нельзя добавить заправку с нулевыми литрами
INSERT INTO fuel_record(car_id, fuel_date, mileage, liters, price_per_liter)
VALUES (1, CURRENT_DATE, 90000, 0, 60.00);

-- 5. Нельзя продублировать правило напоминания для одной машины и одного типа работ
INSERT INTO reminder_rule(car_id, service_type_id, interval_km, interval_months)
VALUES (1, 1, 7500, 12);
