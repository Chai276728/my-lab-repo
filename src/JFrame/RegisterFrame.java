package JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegisterFrame extends JFrame {

    private JTextField userNameField; // 用户名输入框
    private JTextField cardNumberField; // 卡号输入框
    private JPasswordField passwordField; // 密码输入框
    private JComboBox<String> roleComboBox; // 角色下拉框
    private JButton registerButton; // 注册按钮
    private JButton returnLoginButton; // 返回登录按钮

    private static final String URL = "jdbc:mysql://localhost:3306/atm?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
    private static final String USERNAME = "root"; // 数据库用户名
    private static final String PASSWORD = "123456"; // 数据库密码

    private Connection connection; // 数据库连接

    public RegisterFrame() {
        setTitle("银行注册界面");  // 设置窗口标题
        setSize(550, 450);  // 设置窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 设置窗口关闭操作
        setLocationRelativeTo(null);  // 窗口居中显示

        // 初始化数据库连接
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  // 建立数据库连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);

        // 顶部的图片
        ImageIcon imageIcon = new ImageIcon("src/resources/zgyhht.jpg");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 10, 450, 180);
        panel.add(imageLabel);

        // 用户名标签和文本框
        JLabel userNameLabel = new JLabel("用户名:");
        userNameLabel.setBounds(50, 200, 100, 20);
        userNameLabel.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置为宋体，大小19
        panel.add(userNameLabel);

        userNameField = new JTextField();
        userNameField.setBounds(160, 200, 150, 20);
        panel.add(userNameField);

        // 银行账户标签和文本框
        JLabel cardNumberLabel = new JLabel("银行账户:");
        cardNumberLabel.setBounds(50, 230, 100, 20);
        cardNumberLabel.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置为宋体，大小19
        panel.add(cardNumberLabel);

        cardNumberField = new JTextField();
        cardNumberField.setBounds(160, 230, 150, 20);
        panel.add(cardNumberField);

        // 限制cardNumberField只能输入8位数字
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (newStr.matches("\\d{0,8}")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newStr.matches("\\d{0,8}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // 密码标签和文本框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(50, 260, 100, 20);
        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置字体为宋体，大小19
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(160, 260, 150, 20);
        panel.add(passwordField);

        // 限制passwordField只能输入最多6位数字
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (newStr.matches("\\d{0,6}")) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newStr.matches("\\d{0,6}")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // 角色标签和下拉框
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setBounds(50, 290, 150, 20); // 设置位置和大小
        roleLabel.setFont(new Font("宋体", Font.PLAIN, 20)); // 设置字体和大小
        panel.add(roleLabel);

        // 角色选择下拉框
        roleComboBox = new JComboBox<>();
        roleComboBox.addItem("Account");
        roleComboBox.addItem("Admin");
        roleComboBox.setBounds(160, 290, 150, 20); // 设置位置和大小
        panel.add(roleComboBox);

        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.setBounds(50, 330, 100, 30); // 设置位置和大小
        registerButton.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置字体和大小
        panel.add(registerButton);

        // 返回登录界面按钮
        returnLoginButton = new JButton("返回登录界面");
        returnLoginButton.setBounds(160, 330, 150, 30); // 设置位置和大小
        returnLoginButton.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置字体和大小
        panel.add(returnLoginButton);

        // 注册按钮的动作监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText(); // 获取用户名输入框的文本
                String cardNumber = cardNumberField.getText(); // 获取卡号输入框的文本
                String password = new String(passwordField.getPassword()); // 获取密码输入框的文本（注意转换成字符串）
                String role = (String) roleComboBox.getSelectedItem(); // 获取角色下拉框的选项

                if (role.equals("Account")) {
                    // 注册为普通账户
                    registerAsAccount(userName, cardNumber, password);
                } else if (role.equals("Admin")) {
                    // 注册为管理员
                    registerAsAdmin(userName, cardNumber, password);
                }

                JOptionPane.showMessageDialog(RegisterFrame.this,
                        "注册成功！", // 弹出注册成功的提示框
                        "成功", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // 返回登录按钮的动作监听器
        returnLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭注册窗口
                openLoginFrame(); // 打开登录窗口
            }
        });

        setVisible(true); // 设置窗口可见
    }

    // 打开登录界面的方法
    private void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame(); // 创建一个登录界面实例
        loginFrame.setVisible(true); // 设置界面可见
    }

    // 注册为普通账户
    private void registerAsAccount(String userName, String cardNumber, String password) {
        try {
            String sql = "INSERT INTO accounts (user_name, card_number, password, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql); // 准备SQL语句
            statement.setString(1, userName); // 设置用户名
            statement.setString(2, cardNumber); // 设置卡号
            statement.setString(3, password); // 设置密码
            statement.setInt(4, 0); // 初始金额设为0
            statement.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    // 注册为管理员账户
    private void registerAsAdmin(String userName, String cardNumber, String password) {
        try {
            String sql = "INSERT INTO admins (user_name, card_number, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql); // 准备SQL语句
            statement.setString(1, userName); // 设置用户名
            statement.setString(2, cardNumber); // 设置卡号
            statement.setString(3, password); // 设置密码
            statement.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            e.printStackTrace(); // 打印异常信息
        }
    }

    public static void main(String[] args) {
        RegisterFrame registerFrame = new RegisterFrame(); // 创建注册窗口对象
        registerFrame.setVisible(true); // 设置窗口可见
    }
}

