package carservice.dao;

import carservice.db.Database;
import carservice.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Repository {

    public List<User> findUsers() throws SQLException {
        List<User> result = new ArrayList<User>();
        String sql = "SELECT user_id, full_name, email, phone FROM app_user ORDER BY full_name";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new User(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getString("phone")));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public void addUser(String fullName, String email, String phone) throws SQLException {
        String sql = "INSERT INTO app_user(full_name, email, phone) VALUES (?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, emptyToNull(phone));
            ps.executeUpdate();
            ps.close();
        } finally { c.close(); }
    }

    public List<Car> findCarsByUser(int userId) throws SQLException {
        List<Car> result = new ArrayList<Car>();
        String sql = "SELECT car_id, user_id, brand, model, manufacture_year, vin, plate_number, initial_mileage " +
                "FROM car WHERE user_id = ? ORDER BY brand, model";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new Car(rs.getInt("car_id"), rs.getInt("user_id"), rs.getString("brand"),
                        rs.getString("model"), rs.getInt("manufacture_year"), rs.getString("vin"),
                        rs.getString("plate_number"), rs.getInt("initial_mileage")));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public void addCar(int userId, String brand, String model, int year, String vin, String plate, int initialMileage) throws SQLException {
        String sql = "INSERT INTO car(user_id, brand, model, manufacture_year, vin, plate_number, initial_mileage) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, brand);
            ps.setString(3, model);
            ps.setInt(4, year);
            ps.setString(5, emptyToNull(vin));
            ps.setString(6, emptyToNull(plate));
            ps.setInt(7, initialMileage);
            ps.executeUpdate();
            ps.close();
        } finally { c.close(); }
    }

    public List<ServiceType> findServiceTypes() throws SQLException {
        List<ServiceType> result = new ArrayList<ServiceType>();
        String sql = "SELECT service_type_id, type_name, default_interval_km, default_interval_months FROM service_type ORDER BY type_name";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ServiceType(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public List<ServiceRecord> findServices(int carId) throws SQLException {
        List<ServiceRecord> result = new ArrayList<ServiceRecord>();
        String sql = "SELECT sr.service_record_id, sr.car_id, sr.service_type_id, st.type_name, sr.service_date, sr.mileage, sr.cost, sr.comment_text " +
                "FROM service_record sr JOIN service_type st ON st.service_type_id = sr.service_type_id " +
                "WHERE sr.car_id = ? ORDER BY sr.service_date DESC, sr.service_record_id DESC";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ServiceRecord(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4),
                        rs.getDate(5).toLocalDate(), rs.getInt(6), rs.getDouble(7), rs.getString(8)));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public void addService(int carId, int serviceTypeId, LocalDate date, int mileage, double cost, String comment) throws SQLException {
        String sql = "INSERT INTO service_record(car_id, service_type_id, service_date, mileage, cost, comment_text) VALUES (?, ?, ?, ?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setInt(2, serviceTypeId);
            ps.setDate(3, Date.valueOf(date));
            ps.setInt(4, mileage);
            ps.setDouble(5, cost);
            ps.setString(6, emptyToNull(comment));
            ps.executeUpdate();
            ps.close();
        } finally { c.close(); }
    }

    public List<FuelRecord> findFuelRecords(int carId) throws SQLException {
        List<FuelRecord> result = new ArrayList<FuelRecord>();
        String sql = "SELECT fuel_record_id, car_id, fuel_date, mileage, liters, price_per_liter, station_name FROM fuel_record WHERE car_id = ? ORDER BY fuel_date DESC";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new FuelRecord(rs.getInt(1), rs.getInt(2), rs.getDate(3).toLocalDate(),
                        rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7)));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public void addFuel(int carId, LocalDate date, int mileage, double liters, double price, String station) throws SQLException {
        String sql = "INSERT INTO fuel_record(car_id, fuel_date, mileage, liters, price_per_liter, station_name) VALUES (?, ?, ?, ?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setDate(2, Date.valueOf(date));
            ps.setInt(3, mileage);
            ps.setDouble(4, liters);
            ps.setDouble(5, price);
            ps.setString(6, emptyToNull(station));
            ps.executeUpdate();
            ps.close();
        } finally { c.close(); }
    }

    public List<MileageRecord> findMileageRecords(int carId) throws SQLException {
        List<MileageRecord> result = new ArrayList<MileageRecord>();
        String sql = "SELECT mileage_record_id, car_id, record_date, mileage, note FROM mileage_record WHERE car_id = ? ORDER BY record_date DESC";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new MileageRecord(rs.getInt(1), rs.getInt(2), rs.getDate(3).toLocalDate(), rs.getInt(4), rs.getString(5)));
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public void addMileage(int carId, LocalDate date, int mileage, String note) throws SQLException {
        String sql = "INSERT INTO mileage_record(car_id, record_date, mileage, note) VALUES (?, ?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setDate(2, Date.valueOf(date));
            ps.setInt(3, mileage);
            ps.setString(4, emptyToNull(note));
            ps.executeUpdate();
            ps.close();
        } finally { c.close(); }
    }


    public List<ReminderRule> findReminderRules(int carId) throws SQLException {
        List<ReminderRule> result = new ArrayList<ReminderRule>();
        String sql = "SELECT rr.reminder_rule_id, rr.car_id, rr.service_type_id, st.type_name, " +
                "rr.interval_km, rr.interval_months, rr.is_active " +
                "FROM reminder_rule rr " +
                "JOIN service_type st ON st.service_type_id = rr.service_type_id " +
                "WHERE rr.car_id = ? " +
                "ORDER BY st.type_name";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(new ReminderRule(
                        rs.getInt("reminder_rule_id"),
                        rs.getInt("car_id"),
                        rs.getInt("service_type_id"),
                        rs.getString("type_name"),
                        rs.getInt("interval_km"),
                        rs.getInt("interval_months"),
                        rs.getBoolean("is_active")
                ));
            }
            rs.close();
            ps.close();
        } finally {
            c.close();
        }
        return result;
    }

    public void addReminderRule(int carId, int serviceTypeId, int intervalKm, int intervalMonths, boolean active) throws SQLException {
        String sql = "INSERT INTO reminder_rule(car_id, service_type_id, interval_km, interval_months, is_active) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setInt(2, serviceTypeId);
            ps.setInt(3, intervalKm);
            ps.setInt(4, intervalMonths);
            ps.setBoolean(5, active);
            ps.executeUpdate();
            ps.close();
        } finally {
            c.close();
        }
    }

    public void setReminderRuleActive(int ruleId, boolean active) throws SQLException {
        String sql = "UPDATE reminder_rule SET is_active = ? WHERE reminder_rule_id = ?";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setBoolean(1, active);
            ps.setInt(2, ruleId);
            ps.executeUpdate();
            ps.close();
        } finally {
            c.close();
        }
    }

    public List<ReminderItem> findReminderItems(int carId) throws SQLException {
        List<ReminderItem> result = new ArrayList<ReminderItem>();
        String sql = "SELECT rr.interval_km, rr.interval_months, st.type_name, " +
                "(SELECT MAX(sr.mileage) FROM service_record sr WHERE sr.car_id = rr.car_id AND sr.service_type_id = rr.service_type_id) AS last_service_mileage, " +
                "(SELECT MAX(sr.service_date) FROM service_record sr WHERE sr.car_id = rr.car_id AND sr.service_type_id = rr.service_type_id) AS last_service_date, " +
                "(SELECT MAX(mr.mileage) FROM mileage_record mr WHERE mr.car_id = rr.car_id) AS current_mileage " +
                "FROM reminder_rule rr JOIN service_type st ON st.service_type_id = rr.service_type_id " +
                "WHERE rr.car_id = ? AND rr.is_active = TRUE";
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            LocalDate now = LocalDate.now();
            while (rs.next()) {
                int intervalKm = rs.getInt("interval_km");
                int intervalMonths = rs.getInt("interval_months");
                String typeName = rs.getString("type_name");
                int currentMileage = rs.getInt("current_mileage");
                int lastMileage = rs.getInt("last_service_mileage");
                Date lastDateSql = rs.getDate("last_service_date");
                boolean needByKm = currentMileage > 0 && lastMileage > 0 && currentMileage - lastMileage >= intervalKm;
                boolean needByTime = false;
                if (lastDateSql != null) {
                    needByTime = lastDateSql.toLocalDate().plusMonths(intervalMonths).isBefore(now)
                            || lastDateSql.toLocalDate().plusMonths(intervalMonths).isEqual(now);
                }
                if (needByKm || needByTime) {
                    String reason = needByKm ? "по пробегу" : "по времени";
                    result.add(new ReminderItem(typeName + ": требуется ТО " + reason));
                }
            }
            rs.close(); ps.close();
        } finally { c.close(); }
        return result;
    }

    public double getServiceTotal(int carId) throws SQLException {
        return getSingleDouble("SELECT COALESCE(SUM(cost), 0) FROM service_record WHERE car_id = ?", carId);
    }

    public double getFuelTotal(int carId) throws SQLException {
        return getSingleDouble("SELECT COALESCE(SUM(liters * price_per_liter), 0) FROM fuel_record WHERE car_id = ?", carId);
    }

    public double getTotalLiters(int carId) throws SQLException {
        return getSingleDouble("SELECT COALESCE(SUM(liters), 0) FROM fuel_record WHERE car_id = ?", carId);
    }

    private double getSingleDouble(String sql, int carId) throws SQLException {
        Connection c = Database.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            double value = 0;
            if (rs.next()) value = rs.getDouble(1);
            rs.close(); ps.close();
            return value;
        } finally { c.close(); }
    }

    private String emptyToNull(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        return s.trim();
    }
}
