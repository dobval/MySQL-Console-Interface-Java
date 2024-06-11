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
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

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
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                if (connection == null || connection.isClosed()) {
                    String url = properties.getProperty("db.url");
                    connection = DriverManager.getConnection(url, getUser(), getPassword());
                    return connection;
                }
            } catch (SQLException e) {
                retries++;
                System.out.println(e.getMessage() + "\nAttempt: " + retries);
                if (retries >= MAX_RETRIES) {
                    System.out.println("Unable to connect after " + retries + " attempts");
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS); // delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.out.println("Connection retry interrupted. " + ie);
                }
            }
        }
        return null;
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

