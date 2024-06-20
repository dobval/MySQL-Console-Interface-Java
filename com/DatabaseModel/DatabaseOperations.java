package com.DatabaseModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Model (MVC)
public class DatabaseOperations {
    protected Connection conn;

    public DatabaseOperations(Connection conn) {
        this.conn = conn;
    }

    public List<String> listTables() {
        List<String> tables = new ArrayList<>();
        String query = "SHOW TABLES";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    public List<List<String>> selectTable(String tableName) {
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return content;
    }

    public List<String> selectRowById(String tableName, String rowId) {
        List<String> row = new ArrayList<>();
        String query = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, rowId);
            try (ResultSet rs = ps.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();
                if (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return row;
    }

    public List<String> getDistinctCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM products";
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return categories;
    }

    /*
    public List<Customer> selectCustomers() {
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }
    */

    public List<Product> selectProducts(){
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public List<Product> selectProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE category = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    public int getCustomerIdByEmail(String email) {
        String query = "SELECT id FROM customers WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                    }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1; // No customer found
    }

    public List<Order> getCustomerOrders(int customerId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public List<OrderItem> getOrderItems(int orderId) {
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
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderItems;
    }

    public void insertData(String tableName, List<String> columns, List<Object> values) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");
        int columnCount = 0;
        for (String column : columns) {
            if (isNotAutoIncrement(tableName, column)) {
                if (columnCount > 0) {
                    query.append(", ");
                }
                query.append(column);
                columnCount++;
            }
        }
        query.append(") VALUES (");

        for (int i = 0; i < columnCount; i++) {
            if (i > 0) {
                query.append(", ");
            }
            query.append("?");
        }
        query.append(")");

        try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
            int valueIndex = 1;
            for (int i = 0; i < columns.size(); i++) {
                if (isNotAutoIncrement(tableName, columns.get(i))) {
                    ps.setObject(valueIndex, values.get(i));
                    valueIndex++;
                }
            }
            ps.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(query); //debug
    }

    public boolean isNotAutoIncrement(String tableName, String columnName) {
        String query = "SELECT EXTRA FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'arctic_athletes_simple' AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return !rs.getString("EXTRA").contains("auto_increment");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public void deleteData(String tableName, String rowId) {
        String query = "DELETE FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, rowId);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateData(String tableName, String rowId, List<String> columns, List<Object> values) {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");
        for (int i = 0; i < columns.size(); i++) {
            query.append(columns.get(i)).append(" = ?");
            if (i < columns.size() - 1) {
                query.append(", ");
            }
        }
        query.append(" WHERE id = ?");

        try (PreparedStatement ps = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            ps.setString(values.size() + 1, rowId);
            ps.executeUpdate();
            System.out.println("Data updated successfully.");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getColumns(String tableName, String schemaName) {
        List<String> columnNames = new ArrayList<>();
        String qualifiedName = (schemaName != null && !schemaName.isEmpty()) ? (schemaName + "." + tableName) : tableName;

        String query = "SELECT * FROM " + qualifiedName + " WHERE 0=1";

        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnLabel(i));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return columnNames;
    }
}
