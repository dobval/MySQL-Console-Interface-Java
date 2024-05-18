import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final TableOperations tableOperations = new TableOperations();

    public void displayMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a menu entry:");
            System.out.println("1) List all tables");
            System.out.println("2) Select a table");
            System.out.println("3) Delete a table");
            System.out.println("4) Drop a table");
            System.out.println("5) Exit");
            System.out.print("INPUT: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    tableOperations.listTables();
                    break;
                case 2:
                    System.out.print("Enter table name: ");
                    String tableName = scanner.next();
                    tableOperations.selectTable(tableName);
                    break;
                case 3:
                    System.out.print("Enter table name to delete: ");
                    String deleteTableName = scanner.next();
                    tableOperations.deleteTable(deleteTableName);
                    break;
                case 4:
                    System.out.print("Enter table name to drop: ");
                    String dropTableName = scanner.next();
                    tableOperations.dropTable(dropTableName);
                    break;
                case 5:
                    DatabaseManager.closeConnection();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
