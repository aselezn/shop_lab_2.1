import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnector {
    public static final String url = "jdbc:mysql://localhost:3306/shop_db";
    public static final String user = "root";
    public static final String password = "rootpass";

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the database.");
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from the database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
