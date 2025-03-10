package shop.context;

import java.sql.*;

public class DBcontext {

    private final String jdbcURL = "jdbc:sqlserver://localhost\\ADMIN:1433;databaseName=ClothingShopDB;encrypt=false";
    private final String jdbcUsername = "sa";

    private final String jdbcPassword = "12345";

    protected Connection connection;
    protected PreparedStatement statement;
    protected ResultSet resultSet;

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String[] args) {
        try {
            System.out.println(new DBcontext().getConnection());
        } catch (Exception e) {
        }
    }

}
