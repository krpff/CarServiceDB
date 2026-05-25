# Запуск CarServiceDB на Windows

## 1. Установить Java 8

Открыть командную строку и проверить:

```bat
java -version
```

Нужна Java 8.

## 2. Установить PostgreSQL или MySQL

Можно использовать локальный PostgreSQL или MySQL.

Стандартные порты:

```text
PostgreSQL: 5432
MySQL: 3306
```

## 3. Скачать проект

Через Git:

```bat
git clone https://github.com/USERNAME/CarServiceDB.git
cd CarServiceDB
```

Или скачать ZIP с GitHub и распаковать.

## 4. Создать базу PostgreSQL

В DBeaver создать подключение к PostgreSQL.

Создать базу:

```sql
CREATE DATABASE car_service_db;
```

Подключиться к базе `car_service_db`.

Выполнить скрипты:

```text
sql/postgresql/01_create_schema_postgresql.sql
sql/postgresql/02_insert_test_data_postgresql.sql
```

Для проверки ограничений можно выполнить:

```text
sql/postgresql/03_check_constraints_postgresql.sql
```

## 5. Создать базу MySQL

В DBeaver создать подключение к MySQL.

Выполнить скрипты:

```text
sql/mysql/01_create_schema_mysql.sql
sql/mysql/02_insert_test_data_mysql.sql
```

Для проверки ограничений можно выполнить:

```text
sql/mysql/03_check_constraints_mysql.sql
```

## 6. Настроить database.properties

Открыть файл:

```text
config/database.properties
```

Для PostgreSQL:

```properties
db.type=postgresql
db.url=jdbc:postgresql://localhost:5432/car_service_db
db.user=postgres
db.password=your_password
```

Для MySQL:

```properties
db.type=mysql
db.url=jdbc:mysql://localhost:3306/car_service_db?useSSL=false&serverTimezone=UTC
db.user=root
db.password=your_password
```

## 7. Запустить приложение

Открыть командную строку в корне проекта:

```bat
cd C:\path\to\CarServiceDB
```

Запустить:

```bat
java -jar dist\CarServiceDB.jar
```

Важно: запускать нужно из корня проекта, чтобы приложение нашло файл:

```text
config/database.properties
```

## 8. Проверить работу

В приложении выбрать пользователя и автомобиль.

Проверить:
- отображение автомобилей;
- добавление сервисной записи;
- добавление заправки;
- добавление правила ТО;
- отображение напоминаний;
- отображение статистики.

## 9. Проверка через DBeaver

После добавления данных в приложении выполнить:

```sql
SELECT * FROM service_record;
SELECT * FROM fuel_record;
SELECT * FROM mileage_record;
SELECT * FROM reminder_rule;
```

## 10. Частые ошибки

### Приложение не запускается

Проверь версию Java:

```bat
java -version
```

### Нет подключения к базе

Проверь, что:
- сервер PostgreSQL/MySQL запущен;
- база называется `car_service_db`;
- логин и пароль в `config/database.properties` совпадают с DBeaver;
- команда запуска выполняется из корня проекта.

### Не найден database.properties

Запускай так:

```bat
cd C:\path\to\CarServiceDB
java -jar dist\CarServiceDB.jar
```

а не так:

```bat
cd C:\path\to\CarServiceDB\dist
java -jar CarServiceDB.jar
```
