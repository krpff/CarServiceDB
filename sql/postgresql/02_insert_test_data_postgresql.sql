INSERT INTO app_user(full_name, email, phone) VALUES
('mkurapov', 'misha@kurapov.ru', '+79210000001'),
('Анна Соколова', 'anna.sokolova@example.com', '+79990000002'),
('Павел Смирнов', 'pavel.smirnov@example.com', '+79990000003'),
('Мария Орлова', 'maria.orlova@example.com', '+79990000004'),
('Денис Волков', 'denis.volkov@example.com', '+79990000005');

INSERT INTO car(user_id, brand, model, manufacture_year, vin, plate_number, initial_mileage) VALUES
(1, 'Skoda', 'Fabia', 2011, 'XW8EH45J9BK554303', 'Р561КО29', 174000),
(2, 'Kia', 'Rio', 2019, 'KNADM512BK6123452', 'В222ВВ77', 43000),
(3, 'Hyundai', 'Solaris', 2018, 'Z94CT41BBJR123453', 'С333СС77', 52000),
(3, 'Skoda', 'Octavia', 2020, 'TMBAG7NE6L0123454', 'Е444ЕЕ77', 39000),
(4, 'Volkswagen', 'Polo', 2017, 'XW8ZZZ61ZHG123455', 'К555КК77', 69000);

INSERT INTO service_type(type_name, default_interval_km, default_interval_months) VALUES
('Плановое ТО', 7500, 12),
('Замена масла', 7500, 12),
('Замена тормозных колодок', 30000, 24),
('Диагностика подвески', 15000, 12),
('Шиномонтаж', 10000, 6);

INSERT INTO service_record(car_id, service_type_id, service_date, mileage, cost, comment_text) VALUES
(1, 1, '2026-01-15', 175000, 35000.00, 'Плановое обслуживание'),
(2, 2, '2026-03-10', 45000, 6500.00, 'Масло и фильтр'),
(3, 3, '2026-02-05', 55000, 12000.00, 'Передние колодки'),
(4, 4, '2026-04-20', 41000, 3500.00, 'Проверка подвески'),
(5, 5, '2026-05-01', 72000, 4000.00, 'Сезонная замена шин');

INSERT INTO fuel_record(car_id, fuel_date, mileage, liters, price_per_liter, station_name) VALUES
(1, '2026-05-01', 1745000, 42.50, 69.70, 'АЗС 1'),
(2, '2026-05-02', 46200, 38.00, 69.10, 'АЗС 2'),
(3, '2026-05-03', 56000, 40.00, 69.90, 'АЗС 3'),
(4, '2026-05-04', 42000, 45.20, 71.00, 'АЗС 4'),
(5, '2026-05-05', 73000, 39.80, 69.40, 'АЗС 5');

INSERT INTO mileage_record(car_id, record_date, mileage, note) VALUES
(1, '2026-05-01', 174000, 'Начало'),
(2, '2026-05-02', 46200, 'Ежемесячная фиксация'),
(3, '2026-05-03', 56000, 'Контроль пробега'),
(4, '2026-05-04', 42000, 'После ТО'),
(5, '2026-05-05', 73000, 'Плановая запись');

INSERT INTO reminder_rule(car_id, service_type_id, interval_km, interval_months, is_active) VALUES
(1, 1, 7500, 6, TRUE),
(2, 2, 7500, 12, TRUE),
(3, 3, 30000, 24, TRUE),
(4, 4, 15000, 12, TRUE),
(5, 5, 10000, 6, TRUE);
