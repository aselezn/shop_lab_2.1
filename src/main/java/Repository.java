import models.Order;
import models.OrderItem;
import models.Product;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Repository {

    private Product getAllProductWithResultSet(ResultSet resultSet) throws SQLException {

        Product product = null;
        String id = resultSet.getString(1);
        String productName = resultSet.getString(2);
        String color = resultSet.getString(3);
        String price = resultSet.getString(4);
        int balance = resultSet.getInt(5);
        product = new Product(id, productName, color, price, balance);
        return product;
    }

    private Order getAllOrderWithResultSet(ResultSet resultSet) throws SQLException {

        Order order = null;
        long id = resultSet.getLong(1);
        Date createDate = resultSet.getDate(2);
        String customerFullName = resultSet.getString(3);
        String phone = resultSet.getString(4);
        String email = resultSet.getString(5);
        String customerAddress = resultSet.getString(6);
        String status = resultSet.getString(7);
        Date shippingDate = resultSet.getDate(8);
        order = new Order(id, createDate, customerFullName, phone, email,
                customerAddress, status, shippingDate);
        return order;
    }


    private OrderItem getAllrderItemsWithResultSet(ResultSet resultSet) throws SQLException {

        OrderItem orderItem = null;
        Order order = null;
        order = getAllOrderWithResultSet(resultSet);
        Product product = null;
        product = getAllProductWithResultSet(resultSet);
        int orderPrice = resultSet.getInt(3);
        int quantity = resultSet.getInt(4);
        orderItem = new OrderItem(order, product, orderPrice, quantity);
        return orderItem;
    }


    public static List<Product> findAllProducts(Connection con) throws SQLException {
        List<Product> products = new LinkedList<>();
        String query = "SELECT id, product_name, color, price, balance FROM products";

        try (Connection connection = con;
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Product product = new Repository().getAllProductWithResultSet(rs);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    public static List<String> findProductsWithByOrderId(Connection con, long orderId) throws SQLException {
        List<String> products = new ArrayList<>();
        String query = "SELECT oi.order_id, CONCAT(p.product_name, IF(p.color IS NOT NULL, CONCAT(' (', p.color, ')'), '')) AS product_name\n" +
                "FROM orders_items oi " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE oi.order_id = ?";

        try (Connection connection = con;
             PreparedStatement stmt = connection.prepareStatement(query)) {
             stmt.setLong(1, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("product_name");
                    products.add(productName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static void registerOrder(Connection con, String customerFullName, String phone, String email,
                                     String customerAddress, List<String> productIds, List<Integer> quantities) throws SQLException {

        // Создаем объект Order
        LocalDate date = LocalDate.now();
        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // Создаем объект Order
        Order order = new Order(customerFullName, phone, email, customerAddress);
        order.setCreateDate(Date.valueOf(date)); // Устанавливаем текущую дату
        order.setCustomerFullName(customerFullName);
        order.setPhone(phone);
        order.setEmail(email);
        order.setCustomerAddress(customerAddress);
        order.setStatus("P");
        order.setShippingDate(null);

        // Добавляем заказ в таблицу Order
        String insertOrderQuery = "INSERT INTO orders (create_date, customer_fullname, phone, email, " +
                "customer_address, status, shipping_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = con;
             PreparedStatement stmt = connection.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, new Date(order.getCreateDate().getTime()));
            stmt.setString(2, order.getCustomerFullName());
            stmt.setString(3, order.getPhone());
            stmt.setString(4, order.getEmail());
            stmt.setString(5, order.getCustomerAddress());
            stmt.setString(6, order.getStatus());
            stmt.setDate(7, null);

            stmt.executeUpdate();

            // Получаем сгенерированный auto-increment id заказа
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            long orderId;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Failed to insert order, no ID obtained.");
            }

            // Добавляем позиции товаров в таблицу OrderItem
            String insertOrderItemQuery = "INSERT INTO orders_items (order_id, product_id, order_price, quantity) " +
                    "VALUES (?, ?, ?, ?)";

            try (PreparedStatement orderItemStmt = connection.prepareStatement(insertOrderItemQuery)) {
                for (int i = 0; i < productIds.size(); i++) {
                    String productId = productIds.get(i);
                    int quantity = quantities.get(i);

                    // Получаем цену товара по артикулу
                    String getProductPriceQuery = "SELECT price FROM shop_db.products WHERE id = ?";
                    try (PreparedStatement priceStmt = connection.prepareStatement(getProductPriceQuery)) {
                        priceStmt.setString(1, productId);
                        try (ResultSet priceRs = priceStmt.executeQuery()) {
                            if (priceRs.next()) {
                                int orderPrice = priceRs.getInt("price");

                                // Добавляем позицию товара в заказ
                                orderItemStmt.setLong(1, orderId);
                                orderItemStmt.setString(2, productId);
                                orderItemStmt.setInt(3, orderPrice);
                                orderItemStmt.setInt(4, quantity);
                                orderItemStmt.executeUpdate();
                            } else {
                                throw new SQLException("Failed to retrieve product price for product with ID: " + productId);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
