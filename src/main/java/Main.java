import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {

        DataBaseConnector connector = new DataBaseConnector();
        connector.connect();
        Connection connection = connector.getConnection();

//      1. Вывести наименование товаров, по id заказа (если цвет есть - выводим, если нет - не выводим (проверить на id=1))
//        List<String> products_2 = Repository.findProductsWithByOrderId(connection,3);
//        products_2.forEach(System.out::println);

//      2. Регистрация заказа
        String customerFullName = "Mike Doe";
        String phone = "123456789";
        String email = "m.doe@example.com";
        String customerAddress = "12 Main Street";
        List<String> productIds = Arrays.asList("3251615", "3251616");
        List<Integer> quantities = Arrays.asList(2, 1);

        Repository.registerOrder(connection, customerFullName, phone, email, customerAddress, productIds, quantities);
        System.out.println("Order registered successfully.");

    }
}