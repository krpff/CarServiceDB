package carservice.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static DbConfig config;

    public static void init() throws Exception {
        config = DbConfig.load();
        Class.forName(config.getDriverClassName());
    }

    public static Connection getConnection() throws SQLException {
        if (config == null) {
            throw new SQLException("Настройки БД не загружены");
        }
        return DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
    }

    public static DbConfig getConfig() {
        return config;
    }
}
