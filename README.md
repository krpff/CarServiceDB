# CarServiceDB

Приложение для учета обслуживания личного автомобиля.

Программа позволяет:
- вести учет пользователей;
- добавлять автомобили;
- добавлять записи технического обслуживания;
- учитывать заправки;
- вести историю пробега;
- создавать правила напоминаний о ТО;
- просматривать статистику затрат;
- работать с PostgreSQL и MySQL через JDBC.

## Стек

- Java 8
- JavaFX
- JDBC
- PostgreSQL
- MySQL
- DBeaver
- NetBeans

## Структура проекта

```text
CarServiceDB/
├── dist/                         # собранное приложение
│   ├── CarServiceDB.jar
│   └── lib/                      # JDBC-драйверы
├── config/
│   └── database.properties       # настройки подключения к БД
├── sql/
│   ├── postgresql/               # скрипты PostgreSQL
│   └── mysql/                    # скрипты MySQL
├── src/                          # исходный код Java
├── README.md
├── README_macOS.md
└── README_windows.md
```

## Быстрый запуск

1. Установить Java 8.
2. Установить PostgreSQL или MySQL.
3. Создать базу данных `car_service_db`.
4. Выполнить SQL-скрипты из папки `sql`.
5. Заполнить `config/database.properties`.
6. Запустить приложение:

```bash
java -jar dist/CarServiceDB.jar
```

Важно: команду нужно выполнять из корневой папки проекта, где лежат папки `dist`, `config` и `sql`.

## Создание базы данных PostgreSQL

Создать базу:

```sql
CREATE DATABASE car_service_db;
```

Затем подключиться к базе `car_service_db` и выполнить скрипты:

```text
sql/postgresql/01_create_schema_postgresql.sql
sql/postgresql/02_insert_test_data_postgresql.sql
```

Для проверки ограничений можно выполнить:

```text
sql/postgresql/03_check_constraints_postgresql.sql
```

## Настройка подключения PostgreSQL

Файл:

```text
config/database.properties
```

Содержимое:

```properties
db.type=postgresql
db.url=jdbc:postgresql://localhost:5432/car_service_db
db.user=postgres
db.password=your_password
```

## Создание базы данных MySQL

Выполнить скрипты:

```text
sql/mysql/01_create_schema_mysql.sql
sql/mysql/02_insert_test_data_mysql.sql
```

Для проверки ограничений:

```text
sql/mysql/03_check_constraints_mysql.sql
```

## Настройка подключения MySQL

Файл:

```text
config/database.properties
```

Содержимое:

```properties
db.type=mysql
db.url=jdbc:mysql://localhost:3306/car_service_db?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password
```

## Запуск приложения

### macOS / Linux

```bash
cd путь/к/CarServiceDB
java -jar dist/CarServiceDB.jar
```

### Windows

```bat
cd путь\к\CarServiceDB
java -jar dist\CarServiceDB.jar
```

## Переключение между PostgreSQL и MySQL

Для переключения СУБД не нужно менять Java-код. Нужно изменить только файл:

```text
config/database.properties
```

Для PostgreSQL указать:

```properties
db.type=postgresql
db.url=jdbc:postgresql://localhost:5432/car_service_db
```

Для MySQL указать:

```properties
db.type=mysql
db.url=jdbc:mysql://localhost:3306/car_service_db?useSSL=false&serverTimezone=UTC
```

## Работа с приложением

1. Запустить приложение.
2. Выбрать пользователя.
3. Выбрать автомобиль.
4. Добавить сервисную запись.
5. Добавить заправку.
6. Добавить или отключить правило ТО.
7. Проверить напоминания и статистику.

## Таблицы базы данных

В проекте используется 7 таблиц:

- `app_user` — пользователи;
- `car` — автомобили;
- `service_type` — виды сервисных работ;
- `service_record` — история обслуживания;
- `fuel_record` — заправки;
- `mileage_record` — история пробега;
- `reminder_rule` — правила напоминаний о ТО.

## Проверка работы

После добавления данных через приложение можно проверить записи в DBeaver:

```sql
SELECT * FROM app_user;
SELECT * FROM car;
SELECT * FROM service_record;
SELECT * FROM fuel_record;
SELECT * FROM mileage_record;
SELECT * FROM reminder_rule;
SELECT * FROM service_type;
```

