import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableOperations {
    public void listTables() throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW TABLES");

        System.out.println("Tables in the database:");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }

    public void selectTable(String tableName) throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(resultSet.getString(i) + "\t");
            }
            System.out.println();
        }
    }

    public void deleteTable(String tableName) throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM " + tableName);
        System.out.println("Deleted all data from table " + tableName);
    }

    public void dropTable(String tableName) throws SQLException {
        Connection connection = DatabaseManager.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE " + tableName);
        System.out.println("Dropped table " + tableName);
    }
}
