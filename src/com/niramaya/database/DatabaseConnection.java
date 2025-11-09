package niramay.database;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/niramay_healthcare";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root123";
    
    private static Connection connection = null;
    private static boolean connectionTested = false;
    
    static {
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found!");
            showErrorDialog("MySQL JDBC Driver not found. Please add MySQL Connector/J to your project.");
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                if (!connectionTested) {
                    System.out.println("✅ Database connected successfully!");
                    connectionTested = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            showConnectionErrorDialog(e.getMessage());
            return null;
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                // Test with a simple query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1");
                rs.close();
                stmt.close();
                System.out.println("✅ Database connection test: PASSED");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Database connection test: FAILED - " + e.getMessage());
        }
        return false;
    }
    
    private static void showConnectionErrorDialog(String message) {
        JOptionPane.showMessageDialog(null,
            "<html><b>Database Connection Error</b><br><br>" +
            "Unable to connect to the database.<br>" +
            "Error: " + message + "<br><br>" +
            "Please ensure:<br>" +
            "• MySQL server is running<br>" +
            "• Database 'niramay_healthcare' exists<br>" +
            "• Correct username and password</html>",
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }
    
    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null,
            message,
            "System Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
