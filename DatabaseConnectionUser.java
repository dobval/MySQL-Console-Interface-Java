import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionUser {
    private static final String URL = "jdbc:mysql://localhost:3306/arctic_athletes_simple";
    private static final String USER = "guest";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
