import java.sql.Connection;
import java.util.Scanner;

public abstract class Menu {
    public void displayMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayOptions();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            if (!handleChoice(connection, choice)) {
                return;
            }
        }
    }

    protected abstract void displayOptions();
    protected abstract boolean handleChoice(Connection connection, int choice);
}

class EmployeeMenu extends Menu {

    @Override
    protected void displayOptions() {
        System.out.println("\t1. List Tables");
        System.out.println("\t2. Select a Table");
        System.out.println("\t3. Add Data");
        System.out.println("\t4. Modify Data");
        System.out.println("\t5. Delete Data");
        System.out.println("\t6. Logout");
    }

    @Override
    protected boolean handleChoice(Connection conn, int choice) {
        switch (choice) {
            case 1:
                TableOperations.listTables(conn);
                break;
            case 2:
                TableOperations.selectTable(conn);
                break;
            case 3:
                TableOperations.addData(conn);
                break;
            case 4:
                TableOperations.modifyData(conn);
                break;
            case 5:
                TableOperations.removeData(conn);
                break;
            case 6:
                return false;
            default:
                System.out.println("Invalid option. Try again.");
        }
        return true;
    }
}

class UserMenu extends Menu {
    private final String email;

    public UserMenu(String emailLogin) {
        this.email = emailLogin;
    }

    @Override
    protected void displayOptions() {
        System.out.println("\t1. View all products");
        System.out.println("\t2. Browse by category");
        System.out.println("\t3. View my orders");
        System.out.println("\t4. Logout");
    }

    @Override
    protected boolean handleChoice(Connection connection, int choice) {
        switch (choice) {
            case 1:
                TableOperations.selectProducts(connection);
                break;
            case 2:
                TableOperations.selectProductCategory(connection);
                break;
            case 3:
                TableOperations.selectPersonalOrders(connection, this.email);
                break;
            case 4:
                return false;
            default:
                System.out.println("Invalid option. Try again.");
        }
        return true;
    }
}