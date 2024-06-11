import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class DatabaseConnection {
    private static final String CONFIG_FILE = "dbconfig.properties";
    private static final Properties properties = new Properties();
    private static Connection connection = null;

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    protected abstract String getUser();

    protected abstract String getPassword();

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String url = properties.getProperty("db.url");
                connection = DriverManager.getConnection(url, getUser(), getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    protected String getProperty(String key) {
        return properties.getProperty(key);
    }
}

class DatabaseConnectionRoot extends DatabaseConnection {
    @Override
    protected String getUser() {
        return getProperty("db.root.user");
    }

    @Override
    protected String getPassword() {
        return getProperty("db.root.password");
    }
}

class DatabaseConnectionUser extends DatabaseConnection {
    @Override
    protected String getUser() {
        return getProperty("db.user.user");
    }

    @Override
    protected String getPassword() {
        return getProperty("db.user.password");
    }
}

