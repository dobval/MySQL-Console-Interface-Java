import com.DatabaseModel.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// Controller (MVC)
public class TableOperationsController {

    private final DatabaseOperations model;
    private final ConsoleView view;
    //private final Connection conn;

    public TableOperationsController(DatabaseOperations model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    public void listTables(Connection conn) {
        try {
            List<String> tables = model.listTables(conn);
            view.displayTables(tables);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectTable(Connection conn) {
        String tableName = view.promptForTableName();
        try {
            List<List<String>> content = model.selectTable(conn, tableName);
            view.displayTableContent(content);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectProductCategory(Connection conn) {
        try {
            List<String> categories = model.getDistinctCategories(conn);
            view.displayCategories(categories);
            String category = view.promptForCategory();
            List<Product> products = model.selectProductsByCategory(conn, category);
            view.displayProducts(products);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectProducts(Connection conn) {
        try {
            List<Product> products = model.selectProducts(conn);
            view.displayProducts(products);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectPersonalOrders(Connection conn, String email) {
        try {
            int customerId = model.getCustomerIdByEmail(conn, email);
            if (customerId == -1) {
                System.out.println("No customer found with the provided email.");
                return;
            }
            List<Order> orders = model.getCustomerOrders(conn, customerId);
            if (orders.isEmpty()) {
                System.out.println("No orders found for the provided customer.");
                return;
            }
            //TODO simplify method by calling view.displayOrders and view.displayOrderItems
            System.out.println("===Personal Orders===");
            for (Order order : orders) {
                int orderId = order.getId();
                System.out.printf("Order ID: %d, Created: %s, Finished: %s, Status: %s\n",
                        orderId, order.getCreatedDate(), order.getFinishedDate(), order.getStatus());

                List<OrderItem> items = model.getOrderItems(conn, orderId);
                System.out.println(" ==Products in order==");
                for (OrderItem item : items) {
                    String productName = model.getProductNameById(conn, item.getProductId());
                    view.alignProductResults(item.getProductId(), productName, item.getPrice(), item.getQuantity());
                }
            }
            System.out.println("===END===");
            //
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

 //TODO: handle AUTO-INCREMENT IDs for adding data
    public void addData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        try {
            List<String> columns = model.getColumns(conn, tableName, "arctic_athletes_simple");
            List<Object> values = view.promptForColumnValues(columns);
            model.insertData(conn, tableName, columns, values);
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowId();
        try {
            List<String> rows = model.selectRowById(conn, tableName, rowId);
            view.displayRow(rows);
            String confirm = view.promptForConfirmation();
            if (confirm.equals("y")) {
                try {
                    model.deleteData(conn, tableName, rowId);
                    System.out.println("Row with ID " + rowId + " has been deleted from table " + tableName);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // TODO: "Enter" to keep value
    public void modifyData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowIdModify();
        try {
            List<String> columns = model.getColumns(conn, tableName, "arctic_athletes_simple");
            List<Object> values = view.promptForNewColumnValues(columns);
            model.updateData(conn, tableName, rowId, columns, values);
            System.out.println("Data updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
