package com.DatabaseModel;

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
        List<List<String>> content = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                content.add(row);
            }
        }

        return content;
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

    /*
    public List<Customer> selectCustomers(Connection conn) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setPhone(rs.getString("phone"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
        }
        return customers;
    }
    */

    public List<Product> selectProducts(Connection conn) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getBigDecimal("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setCategory(rs.getString("category"));
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> selectProductsByCategory(Connection conn, String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setCategory(rs.getString("category"));
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

    public List<Order> getCustomerOrders(Connection conn, int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setCreatedDate(rs.getDate("created_date"));
                    order.setFinishedDate(rs.getDate("finished_date"));
                    order.setStatus(rs.getString("status"));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(Connection conn, int orderId) throws SQLException {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.product_id, p.name, p.price, oi.quantity " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderId(orderId);
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("name"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setQuantity(rs.getInt("quantity"));
                    orderItems.add(item);
                }
            }
        }
        return orderItems;
    }

    public String getProductNameById(Connection conn, int productId) throws SQLException {
        String query = "SELECT name FROM products WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                } else {
                    return "Unknown Product";
                }
            }
        }
    }

    public void insertData(Connection conn, String tableName, List<String> columns, List<Object> values) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i));
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        query.append(") VALUES (");
        for (int i = 0; i < values.size(); i++) {
            query.append("?");
            if (i < values.size() - 1) {
                query.append(", ");
            }
        }
        query.append(")");

        try (PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }
            pstmt.executeUpdate();
        }
    }

    public void deleteData(Connection conn, String tableName, String rowId) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, rowId);
            pstmt.executeUpdate();
        }
    }

    public void updateData(Connection conn, String tableName, String rowId, List<String> columns, List<Object> values) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i)).append(" = ?");
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE id = ?");

        try (PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }
            pstmt.setString(values.size() + 1, rowId);
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
}
