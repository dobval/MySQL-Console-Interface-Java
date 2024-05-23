import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/arctic_athletes_simple";
    private static Connection connection = null;

    protected abstract String getUser();
    protected abstract String getPassword();

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, getUser(), getPassword());
        }
        return connection;
    }
}

class DatabaseConnectionRoot extends DatabaseConnection {
    private static final String USER = "root";
    private static final String PASSWORD = "adminpassword";

    @Override
    protected String getUser() {
        return USER;
    }

    @Override
    protected String getPassword() {
        return PASSWORD;
    }

}

class DatabaseConnectionUser extends DatabaseConnection {
    private static final String USER = "guest";
    private static final String PASSWORD = "password";

    @Override
    protected String getUser() {
        return USER;
    }

    @Override
    protected String getPassword() {
        return PASSWORD;
    }

}

