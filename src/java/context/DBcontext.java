package context;

import java.sql.*;

public class DBcontext {

    private final String jdbcURL = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=?;encrypt=?";
    private final String jdbcUsername = "?";
    private final String jdbcPassword = "?";

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
