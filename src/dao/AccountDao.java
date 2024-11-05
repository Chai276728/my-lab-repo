package dao;

import java.sql.*;

public class AccountDao {

    private static final String URL = "jdbc:mysql://localhost:3306/atm?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private static Connection connection;

    // 获取数据库连接
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public AccountDao() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getAccountBalance(int cardNumber) throws SQLException {
        double balance = 0.0;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT balance FROM accounts WHERE card_number = ?";
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, cardNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return balance;
    }

    public boolean updateAccountBalance(int cardNumber, double newBalance) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            String updateQuery = "UPDATE accounts SET balance = ? WHERE card_number = ?";
            pstmt = connection.prepareStatement(updateQuery);
            pstmt.setDouble(1, newBalance);
            pstmt.setInt(2, cardNumber);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    public boolean depositMoney(int cardNumber, double depositAmount) throws SQLException {
        double currentBalance = getAccountBalance(cardNumber);
        double newBalance = currentBalance + depositAmount;
        return updateAccountBalance(cardNumber, newBalance);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}

