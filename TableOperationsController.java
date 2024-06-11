import com.DatabaseModel.*;

import java.sql.Connection;
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
        List<String> tables = model.listTables(conn);
        view.displayTables(tables);
    }

    public void selectTable(Connection conn) {
        String tableName = view.promptForTableName();
        List<List<String>> content = model.selectTable(conn, tableName);
        view.displayTableContent(content);

    }

    public void selectProductCategory(Connection conn) {
        List<String> categories = model.getDistinctCategories(conn);
        view.displayCategories(categories);
        String category = view.promptForCategory();
        List<Product> products = model.selectProductsByCategory(conn, category);
        view.displayProducts(products);
    }

    public void selectProducts(Connection conn) {
        List<Product> products = model.selectProducts(conn);
        view.displayProducts(products);
    }

    public void selectPersonalOrders(Connection conn, String email) {
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
        view.displayOrders(orders);
        for (Order order : orders) {
            int orderId = order.getId();
            List<OrderItem> items = model.getOrderItems(conn, orderId);
            view.displayOrderItems(items);
        }
    }

 //TODO: handle AUTO-INCREMENT IDs for adding data
    public void addData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        List<String> columns = model.getColumns(conn, tableName, "arctic_athletes_simple");
        List<Object> values = view.promptForColumnValues(columns);
        model.insertData(conn, tableName, columns, values);
        System.out.println("Data inserted successfully.");
    }

    public void removeData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowId();
        List<String> rows = model.selectRowById(conn, tableName, rowId);
        view.displayRow(rows);
        String confirm = view.promptForConfirmation();
        if (confirm.equals("y")) {
            model.deleteData(conn, tableName, rowId);
            System.out.println("Row with ID " + rowId + " has been deleted from table " + tableName);
        }
    }
    // TODO: "Enter" to keep value
    public void modifyData(Connection conn) {
        listTables(conn);
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowIdModify();
        List<String> columns = model.getColumns(conn, tableName, "arctic_athletes_simple");
        List<Object> values = view.promptForNewColumnValues(columns);
        model.updateData(conn, tableName, rowId, columns, values);
        System.out.println("Data updated successfully.");
    }
}
