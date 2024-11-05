package JFrame;

import dao.AccountDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminAccountInfoManager extends JInternalFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton addButton, deleteButton, updateButton, searchButton;
    private JTextField cardNumberField, userNameField, passwordField, balanceField;

    public AdminAccountInfoManager() {
        setTitle("管理账户信息");
        setSize(700, 500);
        setClosable(true); // 允许关闭
        setMaximizable(true); // 允许最大化
        setResizable(true); // 允许调整大小

        // 创建表格和模型
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // 创建按钮和输入字段
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        updateButton = new JButton("修改");
        searchButton = new JButton("查询");

        cardNumberField = new JTextField(10);
        userNameField = new JTextField(10);
        passwordField = new JTextField(10);
        balanceField = new JTextField(10);

        // 设置按钮和标签的字体大小和样式
        Font buttonFont = new Font("宋体", Font.PLAIN, 20); // 使用宋体，14号，普通样式
        addButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        searchButton.setFont(buttonFont);

        Font labelFont = new Font("宋体", Font.PLAIN, 20); // 使用宋体，14号，普通样式
        JLabel cardLabel = new JLabel("卡号");
        cardLabel.setFont(labelFont);
        JLabel userLabel = new JLabel("用户名");
        userLabel.setFont(labelFont);
        JLabel passLabel = new JLabel("密码");
        passLabel.setFont(labelFont);
        JLabel balanceLabel = new JLabel("余额");
        balanceLabel.setFont(labelFont);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(cardLabel);
        buttonPanel.add(cardNumberField);
        buttonPanel.add(userLabel);
        buttonPanel.add(userNameField);
        buttonPanel.add(passLabel);
        buttonPanel.add(passwordField);
        buttonPanel.add(balanceLabel);
        buttonPanel.add(balanceField);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 添加按钮点击事件监听器
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount();
                refreshTable();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
                refreshTable();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAccount();
                refreshTable();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAccount();
            }
        });

        // 初始化表格数据
        showAllAccounts();
    }

    // 显示所有账户信息
    private void showAllAccounts() {
        model.setColumnIdentifiers(new Object[]{"卡号", "用户名", "密码", "余额"});

        Connection connection = AccountDao.getConnection();
        if (connection != null) {
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {

                while (rs.next()) {
                    int cardNumber = rs.getInt("card_number");
                    String userName = rs.getString("user_name");
                    String password = rs.getString("password");
                    double balance = rs.getDouble("balance");

                    model.addRow(new Object[]{cardNumber, userName, password, balance});
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 添加账户
    private void addAccount() {
        String cardNumberText = cardNumberField.getText();
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String balanceText = balanceField.getText();

        try {
            int cardNumber = Integer.parseInt(cardNumberText);
            double balance = Double.parseDouble(balanceText);

            Connection connection = AccountDao.getConnection();
            if (connection != null) {
                String query = "INSERT INTO accounts (card_number, user_name, password, balance) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, cardNumber);
                    stmt.setString(2, userName);
                    stmt.setString(3, password);
                    stmt.setDouble(4, balance);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "账户添加成功！");
                    clearFields();
                    showAllAccounts();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！");
        }
    }

    // 删除账户
    private void deleteAccount() {
        String cardNumberText = cardNumberField.getText();

        try {
            int cardNumber = Integer.parseInt(cardNumberText);

            Connection connection = AccountDao.getConnection();
            if (connection != null) {
                String query = "DELETE FROM accounts WHERE card_number = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, cardNumber);
                    int rowsDeleted = stmt.executeUpdate();

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "账户删除成功！");
                        clearFields();
                        showAllAccounts();
                    } else {
                        JOptionPane.showMessageDialog(this, "未找到指定卡号的账户！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！");
        }
    }

    // 更新账户信息
    private void updateAccount() {
        String cardNumberText = cardNumberField.getText();
        String userName = userNameField.getText();
        String password = passwordField.getText();
        String balanceText = balanceField.getText();

        try {
            int cardNumber = Integer.parseInt(cardNumberText);
            double balance = Double.parseDouble(balanceText);

            Connection connection = AccountDao.getConnection();
            if (connection != null) {
                String query = "UPDATE accounts SET user_name = ?, password = ?, balance = ? WHERE card_number = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, userName);
                    stmt.setString(2, password);
                    stmt.setDouble(3, balance);
                    stmt.setInt(4, cardNumber);
                    int rowsUpdated = stmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "账户信息更新成功！");
                        clearFields();
                        showAllAccounts();
                    } else {
                        JOptionPane.showMessageDialog(this, "未找到指定卡号的账户！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！");
        }
    }

    // 查询账户信息
    private void searchAccount() {
        String cardNumberText = cardNumberField.getText();

        try {
            int cardNumber = Integer.parseInt(cardNumberText);

            Connection connection = AccountDao.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM accounts WHERE card_number = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, cardNumber);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");
                        double balance = rs.getDouble("balance");

                        userNameField.setText(userName);
                        passwordField.setText(password);
                        balanceField.setText(String.valueOf(balance));
                    } else {
                        JOptionPane.showMessageDialog(this, "未找到指定卡号的账户！");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！");
        }
    }

    // 清空输入字段
    private void clearFields() {
        cardNumberField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        balanceField.setText("");
    }
    // 刷新表格数据模型
    private void refreshTable() {
        // 清空表格模型
        model.setRowCount(0);
        // 重新加载并显示所有账户信息
        showAllAccounts();
    }
}


