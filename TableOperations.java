import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class TableOperations {

    public static void listTables(Connection connection) {
        String query = "SHOW TABLES";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Tables in the database:");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectTable(Connection connection) {
        System.out.print("Enter table name to select: ");
        Scanner scanner = new Scanner(System.in);
        String tableName = scanner.nextLine();
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectProductCategory(Connection connection) {
        // Lists categories
        System.out.println("===Categories===");
        String categoryQuery = "SELECT DISTINCT category FROM products";
        try (PreparedStatement pstmt = connection.prepareStatement(categoryQuery);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String category = rs.getString("category");
                System.out.println(category);
            }
            System.out.println("===END===");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter a category name:");
        String inputCategory = scanner.nextLine();
        selectProductsByCategory(connection, inputCategory);
    }

    public static void selectProducts(Connection connection){
        String productQuery = "SELECT * FROM products";
        try (PreparedStatement pstmt = connection.prepareStatement(productQuery)) {

            int resultCount = 0;

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===Products===");
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int qty = rs.getInt("quantity");

                    alignProductResults(id, name, price, qty);
                    resultCount++;
                }
            }
            System.out.println("\tResults: " + resultCount);
            System.out.println("===END===");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectProductsByCategory(Connection connection, String inputCategory) {
        String productQuery = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(productQuery)) {
            pstmt.setString(1, inputCategory);

            int resultCount = 0;

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n===" + inputCategory + "===");
                while (rs.next()) {

                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double price = rs.getDouble("price");
                    int qty = rs.getInt("quantity");

                    alignProductResults(id, name, price, qty);
                    resultCount++;
                }
            }
            System.out.println("\tResults: " + resultCount);
            System.out.println("===END===");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void alignProductResults(int id, String name, double price, int qty){
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

    public static void selectPersonalOrders(Connection connection, String emailLogin) {
        // Get customer_id from customers where email matches
        String customerQuery = "SELECT id FROM customers WHERE email = ?";
        int customerId = -1;

        // Check for customer_id
        try (PreparedStatement pstmt = connection.prepareStatement(customerQuery)) {
            pstmt.setString(1, emailLogin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customerId = rs.getInt("id");
                } else {
                    System.out.println("No customer found with the provided email.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        // Get all orders for the found customer_id
        String ordersQuery = "SELECT * FROM orders WHERE customer_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(ordersQuery)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("Current user " + emailLogin + ":");
                System.out.println("===Personal Orders===");
                while (rs.next()) {
                    int orderId = rs.getInt("id");
                    String orderCrDate = rs.getString("created_date");
                    String orderFinDate = rs.getString("finished_date");
                    String status = rs.getString("status");

                    System.out.printf("Order ID: %d, Created: %s, Finished: %s, Status: %s\n",
                            orderId, orderCrDate, orderFinDate, status);

                    // Query to get order items and product names for the current order
                    String orderItemsQuery = "SELECT oi.product_id, p.name, p.price, oi.quantity " +
                            "FROM order_items oi " +
                            "JOIN products p ON oi.product_id = p.id " +
                            "WHERE oi.order_id = ?";
                    try (PreparedStatement pstmtOrderItems = connection.prepareStatement(orderItemsQuery)) {
                        pstmtOrderItems.setInt(1, orderId);
                        try (ResultSet rsOrderItems = pstmtOrderItems.executeQuery()) {
                            System.out.println(" ==Products in order==");
                            while (rsOrderItems.next()) {

                                int id = rsOrderItems.getInt("product_id");
                                String name = rsOrderItems.getString("name");
                                double price = rsOrderItems.getDouble("price");
                                int qty = rsOrderItems.getInt("quantity");

                                alignProductResults(id, name, price, qty);
                            }
                        }
                    }
                }
                System.out.println("===END===");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
