package dao;

import model.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminsDao {
    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/atm?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    // 数据库连接对象
    private static Connection connection;

    // 构造函数，初始化数据库连接
    public AdminsDao() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 验证登录账号密码是否正确
    public boolean checkLogin(int cardNumber, int password) {
        String sql = "SELECT * FROM admins WHERE card_number = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cardNumber);
            statement.setInt(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // 如果查询结果有下一行，则账号密码正确
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 注册新管理员账户（静态方法）
    public static boolean registerAdmin(int cardNumber, String name, int password) {
        String sql = "INSERT INTO admins (card_number, name, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, cardNumber);
            statement.setString(2, name);
            statement.setInt(3, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 插入新管理员账户
    public boolean insertAdmin(Admin admin) {
        String query = "INSERT INTO admins (card_number, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, admin.getCardNumber());
            statement.setString(2, admin.getUserName());
            statement.setInt(3, admin.getPassWord()); // 假设 getPassword() 返回 int 类型
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(); // 在 finally 块中关闭连接
        }
    }

    // 根据卡号获取管理员信息
    public Admin getAdminByCardNumber(Integer cardNumber) {
        String query = "SELECT * FROM admins WHERE card_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToAdmin(resultSet); // 将 ResultSet 映射为 Admin 对象
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将 ResultSet 映射为 Admin 对象
    private Admin mapResultSetToAdmin(ResultSet resultSet) throws SQLException {
        Admin admin = new Admin();//创建一个新的 Admin 对象，用于存储从数据库中提取的数据。
        admin.setCardNumber(resultSet.getInt("card_number"));//从结果集中获取名为 "card_number" 的整数值，并将其设置为 Admin 对象的卡号。
        admin.setUserName(resultSet.getString("username"));//从结果集中获取名为 "username" 的字符串值，并将其设置为 Admin 对象的用户名。
        admin.setPassWord(resultSet.getInt("password")); // 从结果集中获取名为 "password" 的整数值，并将其设置为 Admin 对象的密码。

        return admin;//返回填充好数据的 Admin 对象 admin。

    }

    // 关闭数据库连接
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

