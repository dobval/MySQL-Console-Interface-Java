import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TableOperations {

    public static void listTables(Connection conn) {
        String query = "SHOW TABLES";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("===Tables===");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void selectTable(Connection conn) {
        System.out.print("Enter table name to select: ");
        Scanner scanner = new Scanner(System.in);
        String tableName = scanner.nextLine();
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
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

    public static void selectProductCategory(Connection conn) {
        // Lists categories
        System.out.println("===Categories===");
        String categoryQuery = "SELECT DISTINCT category FROM products";
        try (PreparedStatement pstmt = conn.prepareStatement(categoryQuery);
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
        selectProductsByCategory(conn, inputCategory);
    }

    public static void selectProducts(Connection conn){
        String productQuery = "SELECT * FROM products";
        try (PreparedStatement pstmt = conn.prepareStatement(productQuery)) {

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

    public static void selectProductsByCategory(Connection conn, String inputCategory) {
        String productQuery = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(productQuery)) {
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

    public static void selectPersonalOrders(Connection conn, String emailLogin) {
        // Get customer_id from customers where email matches
        String customerQuery = "SELECT id FROM customers WHERE email = ?";
        int customerId = -1;

        // Check for customer_id
        try (PreparedStatement pstmt = conn.prepareStatement(customerQuery)) {
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
        try (PreparedStatement pstmt = conn.prepareStatement(ordersQuery)) {
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
                    try (PreparedStatement pstmtOrderItems = conn.prepareStatement(orderItemsQuery)) {
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

    public static void addData(Connection conn){
        listTables(conn);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a table name:");
        String tableName = scanner.nextLine();
        
        List<String> columnNames = getColumns(conn,tableName, "arctic_athletes_simple");

        if (columnNames == null || columnNames.isEmpty()) {
            System.out.println("No columns found for table: " + tableName);
            return;
        }

        StringBuilder insertColumns = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();
        List<Object> data = new ArrayList<>();
        
        for (String columnName : columnNames) {
            if (isAutoIncrement(conn, tableName, columnName)) {
                continue;
            }

            System.out.print("Enter value for " + columnName + ": ");
            String value = scanner.nextLine();
            data.add(value);

            if (insertColumns.length() > 0) {
                insertColumns.append(", ");
                insertValues.append(", ");
            }

            insertColumns.append(columnName);
            insertValues.append("?");
        }

        String insertSql = "INSERT INTO " + tableName + " (" + insertColumns.toString() + ") VALUES (" + insertValues.toString() + ")";

        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            for (int i = 0; i < data.size(); i++) {
                ps.setObject(i + 1, data.get(i)); // Correctly binding values to the PreparedStatement
            }
            ps.executeUpdate(); // Executes the insertion
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static boolean isAutoIncrement(Connection conn, String tableName, String columnName) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, columnName);
            if (columns.next()) {
                return "YES".equalsIgnoreCase(columns.getString("IS_AUTOINCREMENT"));
            }
        } catch (SQLException e) {
            System.out.println("Error checking if column is auto-increment: " + e.getMessage());
        }
        return false;
    }

    public static List<String> getColumns(Connection conn, String tableName, String schemaName){

        ResultSet rs=null;

        ResultSetMetaData rsmd=null;
        PreparedStatement stmt=null;
        List<String> columnNames =null;
        String qualifiedName = (schemaName!=null&&!schemaName.isEmpty())?(schemaName+"."+tableName):tableName;
        try{
            stmt=conn.prepareStatement("select * from "+qualifiedName+" where 0=1");
            rs=stmt.executeQuery();// Empty ResultSet, but we'll still get the metadata
            rsmd=rs.getMetaData();
            columnNames = new ArrayList<String>();
            for(int i=1;i<=rsmd.getColumnCount();i++)
                columnNames.add(rsmd.getColumnLabel(i));
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        finally{
            if(rs!=null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(stmt!=null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return columnNames;
    }

    public static void removeData(Connection conn) {
        listTables(conn);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a table name:");
        String tableName = scanner.nextLine();
        System.out.println("Enter the ID of the row to delete:");
        String rowId = scanner.nextLine();

        try {
            Statement stmt = conn.createStatement();
            String fetchSql = "SELECT * FROM " + tableName + " WHERE id = " + rowId;
            ResultSet rs = stmt.executeQuery(fetchSql);
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    System.out.println(rsmd.getColumnName(i) + ": " + columnValue);
                }
                System.out.println("Are you sure you want to delete this row? (y/n)");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("y")) {
                    String deleteSql = "DELETE FROM " + tableName + " WHERE id = " + rowId;
                    stmt.executeUpdate(deleteSql);
                    System.out.println("Row with ID " + rowId + " has been deleted from table " + tableName);
                } else {
                    System.out.println("Deletion cancelled.");
                }
            } else {
                System.out.println("No row with ID " + rowId + " found in table " + tableName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void modifyData(Connection conn) {
        listTables(conn);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a table name:");
        String tableName = scanner.nextLine();

        List<String> columnNames = getColumns(conn, tableName, "arctic_athletes_simple");

        if (columnNames == null || columnNames.isEmpty()) {
            System.out.println("No columns found for table: " + tableName);
            return;
        }

        System.out.println("Enter the ID of the row to modify:");
        String rowId = scanner.nextLine();

        try (Statement stmt = conn.createStatement()) {
            String fetchSql = "SELECT * FROM " + tableName + " WHERE id = " + rowId;
            ResultSet rs = stmt.executeQuery(fetchSql);
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    System.out.println(rsmd.getColumnName(i) + ": " + columnValue);
                }

                StringBuilder updateSql = new StringBuilder("UPDATE " + tableName + " SET ");
                List<Object> data = new ArrayList<>();
                boolean first = true;

                for (String columnName : columnNames) {
                    if (isAutoIncrement(conn, tableName, columnName)) {
                        continue;
                    }

                    System.out.print("Enter new value for " + columnName + " (or press Enter to keep the current value): ");
                    String value = scanner.nextLine();

                    if (!value.isEmpty()) {
                        if (!first) {
                            updateSql.append(", ");
                        }
                        updateSql.append(columnName).append(" = ?");
                        data.add(value);
                        first = false;
                    }
                }

                updateSql.append(" WHERE id = ?");

                try (PreparedStatement ps = conn.prepareStatement(updateSql.toString())) {
                    for (int i = 0; i < data.size(); i++) {
                        ps.setObject(i + 1, data.get(i));
                    }
                    ps.setObject(data.size() + 1, rowId);
                    ps.executeUpdate();
                    System.out.println("Data updated successfully.");
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println("No row with ID " + rowId + " found in table " + tableName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
