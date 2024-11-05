package dao;

import model.Account;
import model.Transfer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferDao {
    private static final String URL = "jdbc:mysql://localhost:3306/atm?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    private Connection connection;

    public TransferDao() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取特定卡号相关的转账信息列表
    public List<Transfer> getTransfersByCardNumber(int cardNumber) {
        List<Transfer> transfers = new ArrayList<>();
        String query = "SELECT * FROM transfer WHERE sender_card_number = ? OR receiver_card_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cardNumber);
            stmt.setInt(2, cardNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int senderCardNumber = rs.getInt("sender_card_number");
                int receiverCardNumber = rs.getInt("receiver_card_number");
                double amount = rs.getDouble("amount");

                Transfer transfer = new Transfer(senderCardNumber, receiverCardNumber, amount);
                transfers.add(transfer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transfers;
    }


    // 获取账户信息
    public Account getAccountInfo(int cardNumber) {
        String query = "SELECT * FROM accounts WHERE card_number = ?";
        Account account = null;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, cardNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                account = new Account(cardNumber, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    // 记录转账操作
    public boolean recordTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (sender_card_number, receiver_card_number, amount) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transfer.getSenderCardNumber());
            stmt.setInt(2, transfer.getReceiverCardNumber());
            stmt.setDouble(3, transfer.getAmount());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // 处理 SQL 异常，可以记录日志或者抛出自定义异常
            return false;
        }
    }

    // 转账操作
    public boolean transfer(int senderCardNumber, int receiverCardNumber, double amount) {
        try {
            connection.setAutoCommit(false); // 开启事务

            // 检查发送账户余额是否足够
            double senderBalance = getBalance(senderCardNumber);
            if (senderBalance < amount) {
                throw new IllegalStateException("账户余额不足");
            }

            // 更新发送账户余额
            double newSenderBalance = senderBalance - amount;
            boolean senderUpdated = updateBalance(senderCardNumber, newSenderBalance);

            // 更新接收账户余额
            double receiverBalance = getBalance(receiverCardNumber);
            double newReceiverBalance = receiverBalance + amount;
            boolean receiverUpdated = updateBalance(receiverCardNumber, newReceiverBalance);

            // 提交事务
            if (senderUpdated && receiverUpdated) {
                connection.commit();
                return true;
            } else {
                connection.rollback(); // 回滚事务
                return false;
            }
        } catch (SQLException e) {
            try {
                connection.rollback(); // 回滚事务
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // 恢复自动提交模式
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    // 获取账户余额
    public double getBalance(int cardNumber) {
        double balance = 0;
        String query = "SELECT balance FROM accounts WHERE card_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // 更新账户余额
    public boolean updateBalance(int cardNumber, double newBalance) {
        String query = "UPDATE accounts SET balance = ? WHERE card_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, newBalance);
            statement.setInt(2, cardNumber);
            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 关闭数据库连接
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}





