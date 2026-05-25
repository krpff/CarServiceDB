package carservice.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DbConfig {
    private final String type;
    private final String url;
    private final String user;
    private final String password;

    private DbConfig(String type, String url, String user, String password) {
        this.type = type;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DbConfig load() throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("config/database.properties");
        try {
            props.load(in);
        } finally {
            in.close();
        }
        return new DbConfig(
                props.getProperty("db.type", "postgresql").trim(),
                props.getProperty("db.url", "").trim(),
                props.getProperty("db.user", "").trim(),
                props.getProperty("db.password", "")
        );
    }

    public String getType() { return type; }
    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }

    public String getDriverClassName() {
        if ("mysql".equalsIgnoreCase(type)) {
            return "com.mysql.cj.jdbc.Driver";
        }
        return "org.postgresql.Driver";
    }
}
