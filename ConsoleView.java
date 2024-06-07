import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// View (MVC)
public class ConsoleView {

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void displayTables(List<String> tables) {
        System.out.println("=== Tables ===");
        for (String table : tables) {
            System.out.println(table);
        }
        System.out.println("=== END ===");
    }

    public String promptForTableName() {
        System.out.print("Enter table name: ");
        return scanner.nextLine();
    }

    public void displayTableContent(List<List<String>> content) {
        for (List<String> row : content) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public void displayCategories(List<String> categories) {
        System.out.println("===Categories===");
        for (String category : categories) {
            System.out.println(category);
        }
        System.out.println("===END===");
    }

    public String promptForCategory() {
        System.out.println("\nEnter a category name:");
        return scanner.nextLine();
    }

    public void displayProducts(List<List<String>> products) {
        System.out.println("\n===Products===");
        for (List<String> product : products) {
            int id = Integer.parseInt(product.get(0));
            String name = product.get(1);
            double price = Double.parseDouble(product.get(2));
            int qty = Integer.parseInt(product.get(3));
            alignProductResults(id, name, price, qty);
        }
        System.out.println("===END===");
    }

    public void alignProductResults(int id, String name, double price, int qty){
        // Breaks the name string on the last whitespace for better readability
        int lineBreakIndex = 30;
        if (name.length() > lineBreakIndex) {
            int splitIndex = name.lastIndexOf(' ', lineBreakIndex);
            splitIndex = splitIndex != -1 ? splitIndex : lineBreakIndex;
            String part1 = name.substring(0, splitIndex).trim();
            String part2 = name.substring(splitIndex).trim();
            System.out.printf("%-30s |\n %-29s | Price: %10.2f BGN | Qty: %5d | Ref.No: %5d\n",
                    part1, part2, price, qty, id);
        } else {
            System.out.printf("%-30s | Price: %10.2f BGN | Qty: %5d | Ref.No: %5d\n",
                    name, price, qty, id);
        }
    }
    public void displayOrders(List<List<String>> orders) {
        System.out.println("===Personal Orders===");
        for (List<String> order : orders) {
            System.out.printf("Order ID: %s, Created: %s, Finished: %s, Status: %s\n",
                    order.get(0), order.get(1), order.get(2), order.get(3));
        }
    }

    public void displayOrderItems(List<List<String>> items) {
        System.out.println(" ==Products in order==");
        for (List<String> item : items) {
            System.out.printf("%-30s | Price: %10.2f BGN | Qty: %5d | Ref.No: %5d\n",
                    item.get(1), Double.parseDouble(item.get(2)), Integer.parseInt(item.get(3)), Integer.parseInt(item.get(0)));
        }
        System.out.println("===END===");
    }

    public String promptForRowId() {
        System.out.println("Enter the ID of the row to delete:");
        return scanner.nextLine();
    }

    public void displayRow(List<String> row) {
        if (row.isEmpty()) {
            System.out.println("No row found with the provided ID.");
        } else {
            for (String columnValue : row) {
                System.out.print(columnValue + " ");
            }
            System.out.println();
        }
    }

    public String promptForConfirmation() {
        System.out.println("Are you sure you want to delete this row? (y/n)");
        return scanner.nextLine();
    }

    public List<Object> promptForColumnValues(List<String> columns) {
        List<Object> values = new ArrayList<>();
        for (String column : columns) {
            if (column.equals("id")){
                System.out.println("ID is auto-incremented when inserting data.");
            }
            System.out.print("Enter value for " + column + ": ");
            values.add(scanner.nextLine());
        }
        return values;
    }

    public String promptForRowIdModify() {
        System.out.println("Enter the ID of the row to modify:");
        return scanner.nextLine();
    }

    public List<Object> promptForNewColumnValues(List<String> columns) {
        List<Object> values = new ArrayList<>();
        for (String column : columns) {
            System.out.print("Enter new value for " + column + " (or press Enter to keep the current value): ");
            String value = scanner.nextLine();
            if (!value.isEmpty()) {
                values.add(value);
            }
        }
        return values;
    }
}