import java.sql.Connection;
import java.util.Scanner;

public class UserMenu {
    String email;

    public UserMenu(String emailLogin){
        email = emailLogin;
    }

    public void displayMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\t1. View all products");
            System.out.println("\t2. Browse by category");
            System.out.println("\t3. View my orders");
            System.out.println("\t4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    TableOperations.selectProducts(connection); // Adjust to view only products table
                    break;
                case 2:
                    TableOperations.selectProductCategory(connection);
                    break;
                case 3:
                    TableOperations.selectPersonalOrders(connection, this.email);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
