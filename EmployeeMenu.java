import java.sql.Connection;
import java.util.Scanner;

public class EmployeeMenu {

    public void displayMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\t1. List Tables");
            System.out.println("\t2. Select a Table");
            System.out.println("\t3. Add Data");
            System.out.println("\t4. Modify Data");
            System.out.println("\t5. Delete Data");
            System.out.println("\t6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    TableOperations.listTables(connection);
                    break;
                case 2:
                    TableOperations.selectTable(connection);
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
