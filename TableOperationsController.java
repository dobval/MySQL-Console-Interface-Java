import com.DatabaseModel.*;

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

    public void listTables() {
        List<String> tables = model.listTables();
        view.displayTables(tables);
    }

    public void selectTable() {
        String tableName = view.promptForTableName();
        List<List<String>> content = model.selectTable(tableName);
        view.displayTableContent(content);

    }

    public void selectProductCategory() {
        List<String> categories = model.getDistinctCategories();
        view.displayCategories(categories);
        String category = view.promptForCategory();
        List<Product> products = model.selectProductsByCategory(category);
        view.displayProducts(products);
    }

    public void selectProducts() {
        List<Product> products = model.selectProducts();
        view.displayProducts(products);
    }

    public void selectPersonalOrders(String email) {
        int customerId = model.getCustomerIdByEmail(email);
        if (customerId == -1) {
            System.out.println("No customer found with the provided email.");
            return;
        }
        List<Order> orders = model.getCustomerOrders(customerId);
        if (orders.isEmpty()) {
            System.out.println("No orders found for the provided customer.");
            return;
        }
        view.displayOrders(orders);
        for (Order order : orders) {
            int orderId = order.getId();
            List<OrderItem> items = model.getOrderItems(orderId);
            view.displayOrderItems(items);
        }
    }

    public void addData() {
        listTables();
        String tableName = view.promptForTableName();
        List<String> columns = model.getColumns(tableName, "arctic_athletes_simple");
        List<Object> values = view.promptForColumnValues(columns);
        model.insertData(tableName, columns, values);
    }

    public void removeData() {
        listTables();
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowId();
        List<String> rows = model.selectRowById(tableName, rowId);
        view.displayRow(rows);
        String confirm = view.promptForConfirmation();
        if (confirm.equals("y")) {
            model.deleteData(tableName, rowId);
            System.out.println("Row with ID " + rowId + " has been deleted from table " + tableName);
        }
    }

    public void modifyData() {
        listTables();
        String tableName = view.promptForTableName();
        String rowId = view.promptForRowIdModify();

        // Get current row data
        List<String> currentRow = model.selectRowById(tableName, rowId);
        view.displayRow(currentRow);

        List<String> columns = model.getColumns(tableName, "arctic_athletes_simple");
        List<Object> values = view.promptForNewColumnValuesWithDefaults(columns, currentRow);
        model.updateData(tableName, rowId, columns, values);
    }
}
