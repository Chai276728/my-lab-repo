package JFrame;

import dao.AccountDao;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InquireMoney extends JFrame {

    private int cardNumber;
    private int passWord;

    public InquireMoney(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;
        this.passWord = passWord;

        // 初始化界面
        initialize();
    }

    private void initialize() {
        setTitle("查询余额");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // 设置布局为null，使用绝对布局

        // 创建标签用于显示账号
        JLabel cardNumberLabel = new JLabel("账号:");
        cardNumberLabel.setBounds(20, 20, 100, 30); // 设置位置和大小
        add(cardNumberLabel);

        JLabel cardNumberValueLabel = new JLabel(String.valueOf(cardNumber));
        cardNumberValueLabel.setBounds(120, 20, 150, 30); // 设置位置和大小
        add(cardNumberValueLabel);

        // 创建标签用于显示余额信息
        JLabel balanceLabel = new JLabel("账户余额:");
        balanceLabel.setBounds(20, 60, 100, 30); // 设置位置和大小
        add(balanceLabel);

        JLabel balanceValueLabel = new JLabel();
        balanceValueLabel.setBounds(120, 60, 150, 30); // 设置位置和大小
        add(balanceValueLabel);

        // 查询数据库获取余额并显示
        double balance = queryBalance(cardNumber);
        balanceValueLabel.setText(String.valueOf(balance));
    }

    // 查询数据库获取余额的方法
    private double queryBalance(int cardNumber) {
        double balance = 0.0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // 获取数据库连接
            connection = AccountDao.getConnection(); // 获取数据库连接
            // 准备查询语句，根据卡号查询余额
            String query = "SELECT balance FROM accounts WHERE card_number = ?";
            statement = connection.prepareStatement(query); // 准备预处理语句
            statement.setInt(1, cardNumber); // 设置卡号参数
            resultSet = statement.executeQuery(); // 执行查询

            // 处理查询结果
            if (resultSet.next()) {
                balance = resultSet.getDouble("balance"); // 获取查询结果中的余额字段值
            } else {
                JOptionPane.showMessageDialog(null, "未找到卡号对应的账户"); // 如果未找到对应记录，显示提示信息
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "查询余额时出错"); // 如果查询过程中出现异常，显示错误信息
        } finally {
            // 关闭资源
            try {
                if (resultSet != null) resultSet.close(); // 关闭结果集
                if (statement != null) statement.close(); // 关闭预处理语句
                // 注意：这里不关闭连接，以便后续重复使用连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return balance; // 返回查询到的余额值
    }
}



