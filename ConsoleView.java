import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.DatabaseModel.*;

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
        System.out.print("===END===");
    }

    public String promptForCategory() {
        System.out.println("\nEnter a category name:");
        return scanner.nextLine();
    }

    public void displayProducts(List<Product> products) {
        System.out.println("\n=== Products ===");
        for (Product product : products) {
            int id = product.getId();
            String name = product.getName();
            BigDecimal price = product.getPrice();
            int qty = product.getQuantity();
            alignProductResults(id, name, price, qty);
        }
        System.out.println("=== END ===");
    }

    public void alignProductResults(int id, String name, BigDecimal price, int qty){
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

    public void displayOrders(List<Order> orders) {
        System.out.println("\n=== Orders ===");
        for (Order order : orders) {
            printCustomerFormatOrder(order);
        }
    }

    public void printCustomerFormatOrder(Order order) {
        System.out.printf("Order ID: %d, Created: %s, Finished: %s, Status: %s%n",
                order.getId(), order.getCreatedDate(), order.getFinishedDate(), order.getStatus());
    }

    public void displayOrderItems(List<OrderItem> orderItems) {
        System.out.println("\n== Order Items ==");
        for (OrderItem item : orderItems) {
            int id = item.getId();
            String name = item.getProductName();
            BigDecimal price = item.getPrice();
            int qty = item.getQuantity();
            alignProductResults(id, name, price, qty);
        }
        System.out.println("=== END ===");
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

    public List<Object> promptForNewColumnValuesWithDefaults(List<String> columns, List<String> currentRow) {
        List<Object> values = new ArrayList<>();
        int index = 0; // For current values
        for (String column : columns) {
            System.out.print("Enter new value for " + column + " (Enter to keep value): ");
            String value = scanner.nextLine();
            if (value.isEmpty()) {
                values.add(currentRow.get(index));
            }
            else {
                values.add(value);
            }
            index++;
        }
        return values;
    }
}
