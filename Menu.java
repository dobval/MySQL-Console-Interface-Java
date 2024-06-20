import com.DatabaseModel.DatabaseOperations;

import java.sql.Connection;
import java.util.Scanner;

public abstract class Menu {
    protected Connection conn;
    protected DatabaseOperations model;
    protected ConsoleView view;
    protected TableOperationsController controller;

    public Menu(Connection conn) {
        this.conn = conn;
        this.model = new DatabaseOperations(conn);
        this.view = new ConsoleView();
        this.controller = new TableOperationsController(model, view);
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayOptions();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (!handleChoice(choice)) break;
        }
    }

    protected abstract void displayOptions();
    protected abstract boolean handleChoice(int choice);
}

class EmployeeMenu extends Menu {

    public EmployeeMenu(Connection conn) {
        super(conn);
    }

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
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                controller.listTables();
                break;
            case 2:
                controller.selectTable();
                break;
            case 3:
                controller.addData();
                break;
            case 4:
                controller.modifyData();
                break;
            case 5:
                controller.removeData();
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

    public UserMenu(Connection conn, String emailLogin) {
        super(conn);
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
    protected boolean handleChoice(int choice) {
        switch (choice) {
            case 1:
                controller.selectProducts();
                break;
            case 2:
                controller.selectProductCategory();
                break;
            case 3:
                controller.selectPersonalOrders(this.email);
                break;
            case 4:
                return false;
            default:
                System.out.println("Invalid option. Try again.");
        }
        return true;
    }
}