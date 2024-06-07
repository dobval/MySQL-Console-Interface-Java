import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Model (MVC)
public class DatabaseOperations {

    public List<String> listTables(Connection conn) throws SQLException {
        List<String> tables = new ArrayList<>();
        String query = "SHOW TABLES";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        return tables;
    }

    public List<List<String>> selectTable(Connection conn, String tableName) throws SQLException {
        List<List<String>> result = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                result.add(row);
            }
        }
        return result;
    }

    public List<String> selectRowById(Connection conn, String tableName, String rowId) throws SQLException {
        List<String> row = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, rowId);
            try (ResultSet rs = pstmt.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();
                if (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                }
            }
        }
        return row;
    }

    public List<String> getDistinctCategories(Connection conn) throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM products";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    public List<List<String>> selectProducts(Connection conn) throws SQLException {
        List<List<String>> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                List<String> product = new ArrayList<>();
                product.add(String.valueOf(rs.getInt("id")));
                product.add(rs.getString("name"));
                product.add(String.valueOf(rs.getDouble("price")));
                product.add(String.valueOf(rs.getInt("quantity")));
                products.add(product);
            }
        }
        return products;
    }

    public List<List<String>> selectProductsByCategory(Connection conn, String category) throws SQLException {
        List<List<String>> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    List<String> product = new ArrayList<>();
                    product.add(String.valueOf(rs.getInt("id")));
                    product.add(rs.getString("name"));
                    product.add(String.valueOf(rs.getDouble("price")));
                    product.add(String.valueOf(rs.getInt("quantity")));
                    products.add(product);
                }
            }
        }
        return products;
    }

    public int getCustomerIdByEmail(Connection conn, String email) throws SQLException {
        String query = "SELECT id FROM customers WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    return -1; // No customer found
                }
            }
        }
    }

    public List<List<String>> getCustomerOrders(Connection conn, int customerId) throws SQLException {
        List<List<String>> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    List<String> order = new ArrayList<>();
                    order.add(String.valueOf(rs.getInt("id")));
                    order.add(rs.getString("created_date"));
                    order.add(rs.getString("finished_date"));
                    order.add(rs.getString("status"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public List<List<String>> getOrderItems(Connection conn, int orderId) throws SQLException {
        List<List<String>> orderItems = new ArrayList<>();
        String query = "SELECT oi.product_id, p.name, p.price, oi.quantity " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    List<String> item = new ArrayList<>();
                    item.add(String.valueOf(rs.getInt("product_id")));
                    item.add(rs.getString("name"));
                    item.add(String.valueOf(rs.getDouble("price")));
                    item.add(String.valueOf(rs.getInt("quantity")));
                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    public void insertData(Connection conn, String tableName, List<String> columns, List<Object> values) throws SQLException {
        /*List<String> filteredColumns = new ArrayList<>();
        List<Object> filteredValues = new ArrayList<>();

        for (int i = 0; i < columns.size(); i++) {
            if (!isAutoIncrement(conn, tableName, columns.get(i))) {
                filteredColumns.add(columns.get(i));
                filteredValues.add(values.get(i));
            }
        }*/

        StringBuilder insertColumns = new StringBuilder();
        StringBuilder insertValues = new StringBuilder();

        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                insertColumns.append(", ");
                insertValues.append(", ");
            }
            insertColumns.append(columns.get(i));
            insertValues.append("?");
        }

        String insertSql = "INSERT INTO " + tableName + " (" + insertColumns + ") VALUES (" + insertValues + ")";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }
            pstmt.executeUpdate();
        }
    }

    public void deleteData(Connection conn, String tableName, String rowId) throws SQLException {
        String deleteSql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, rowId);
            pstmt.executeUpdate();
        }
    }

    public void updateData(Connection conn, String tableName, String rowId, List<String> columns, List<Object> values) throws SQLException {
        StringBuilder updateSql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                updateSql.append(", ");
            }
            updateSql.append(columns.get(i)).append(" = ?");
        }
        updateSql.append(" WHERE id = ?");

        try (PreparedStatement pstmt = conn.prepareStatement(updateSql.toString())) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }
            pstmt.setObject(values.size() + 1, rowId);
            pstmt.executeUpdate();
        }
    }

    public List<String> getColumns(Connection conn, String tableName, String schemaName) {

        ResultSet rs = null;

        ResultSetMetaData rsmd;
        PreparedStatement ps = null;
        List<String> columnNames = null;
        String qualifiedName = (schemaName != null && !schemaName.isEmpty()) ? (schemaName + "." + tableName) : tableName;
        try {
            ps = conn.prepareStatement("select * from " + qualifiedName + " where 0=1");
            rs = ps.executeQuery();// Empty ResultSet, but we'll still get the metadata
            rsmd = rs.getMetaData();
            columnNames = new ArrayList<>();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
                columnNames.add(rsmd.getColumnLabel(i));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return columnNames;
    }

    //check TODO
    private boolean isAutoIncrement(Connection conn, String tableName, String columnName) {
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
}
