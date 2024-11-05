package JFrame;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField cardNumberField; // 银行账户文本框
    private JPasswordField passwordField; // 密码文本框
    private JButton loginButton; // 登录按钮
    private JButton registerButton; // 注册按钮

    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/atm?serverTimezone=GMT%2B8&useSSL=true&characterEncoding=utf8";
    private static final String USERNAME = "root"; // 数据库用户名
    private static final String PASSWORD = "123456"; // 数据库密码

    private Connection connection; // 数据库连接对象

    public LoginFrame() {
        setTitle("银行登录界面"); // 设置窗口标题
        setSize(550, 400); // 设置窗口大小，以适应更大的图片
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        setLocationRelativeTo(null); // 将窗口设置为屏幕中央显示

        // 初始化数据库连接
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // 建立数据库连接
        } catch (SQLException e) {
            e.printStackTrace(); // 连接异常时打印错误信息
        }

        // 创建面板用于放置组件
        JPanel panel = new JPanel();
        panel.setLayout(null); // 使用空布局管理器
        getContentPane().add(panel); // 将面板添加到窗口的内容面板中

        // 设置顶部图片
        ImageIcon imageIcon = new ImageIcon("src/resources/zgyhht.jpg"); // 图片路径
        JLabel imageLabel = new JLabel(imageIcon); // 创建标签显示图片
        imageLabel.setBounds(50, 10, 450, 180); // 设置图片标签的位置和大小
        panel.add(imageLabel); // 将图片标签添加到面板中

        // 创建标签和输入框
        JLabel cardNumberLabel = new JLabel("银行账户:"); // 创建账户标签
        cardNumberLabel.setBounds(50, 210, 120, 25); // 设置标签的位置和大小
        cardNumberLabel.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置标签字体和大小
        panel.add(cardNumberLabel); // 将账户标签添加到面板中

        cardNumberField = new JTextField(); // 创建账户文本框
        cardNumberField.setBounds(180, 210, 150, 25); // 设置文本框的位置和大小
        panel.add(cardNumberField); // 将账户文本框添加到面板中

        // 限制账号中输入最多8位数字
        ((AbstractDocument) cardNumberField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                // 获取当前文本框中的全部文本
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                // 检查新字符串是否符合最多8位数字的条件
                if (newStr.matches("\\d{0,8}")) {
                    // 如果符合条件，则允许插入操作
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // 获取当前文本框中的全部文本
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                // 检查新字符串是否符合最多8位数字的条件
                if (newStr.matches("\\d{0,8}")) {
                    // 如果符合条件，则允许替换操作
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(50, 250, 120, 25); // 调整垂直位置和大小
        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 19)); // 设置字体和大小
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(180, 250, 150, 25); // 调整垂直位置和大小
        panel.add(passwordField);

        // 限制密码字段最多输入6位数字
        ((AbstractDocument) passwordField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                // 获取当前文本框中的内容并与新输入的字符合并
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                // 检查新字符串是否匹配最多6位数字的格式
                if (newStr.matches("\\d{0,6}")) {
                    // 调用父类方法插入字符串
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                // 获取当前文本框中的内容并与替换的文本合并
                String newStr = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                // 检查新字符串是否匹配最多6位数字的格式
                if (newStr.matches("\\d{0,6}")) {
                    // 调用父类方法替换字符串
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        // 按钮
        loginButton = new JButton("登录");
        loginButton.setBounds(100, 300, 120, 35); // 调整垂直位置和大小
        loginButton.setFont(new Font("宋体", Font.BOLD, 19)); // 设置字体和大小
        panel.add(loginButton);

        registerButton = new JButton("注册");
        registerButton.setBounds(280, 300, 120, 35); // 调整垂直位置和大小
        registerButton.setFont(new Font("宋体", Font.BOLD, 19)); // 设置字体和大小
        panel.add(registerButton);

        // 添加登录按钮的事件监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 尝试获取并解析输入的卡号和密码
                    Integer cardNumber = Integer.parseInt(cardNumberField.getText()); // 获取卡号
                    Integer password = Integer.parseInt(new String(passwordField.getPassword())); // 获取密码

                    // 尝试使用输入的卡号和密码进行账户登录
                    if (loginAsAccount(cardNumber, password)) {
                        // 如果作为账户登录成功
                        dispose(); // 关闭登录窗口
                        AccountFrame accountFrm = new AccountFrame(cardNumber, password); // 创建账户窗口
                        accountFrm.setVisible(true); // 显示账户窗口
                    } else if (loginAsAdmin(cardNumber, password)) {
                        // 如果作为管理员登录成功
                        dispose(); // 关闭登录窗口
                        AdminFrame adminFrm = new AdminFrame(); // 创建管理员窗口
                        adminFrm.setVisible(true); // 显示管理员窗口
                    } else {
                        // 登录失败，显示错误消息框
                        JOptionPane.showMessageDialog(LoginFrame.this,
                                "登录失败。卡号或密码错误。",
                                "登录错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    // 捕获输入格式错误异常，显示错误消息框
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "输入无效。请输入有效的卡号和密码。",
                            "输入错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // 添加注册按钮的事件监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭登录窗口
                RegisterFrame registerFrame = new RegisterFrame(); // 创建注册窗口对象
                registerFrame.setVisible(true); // 显示注册窗口
            }
        });
    }

    /**
     * 尝试以账户身份登录
     * @param cardNumber 卡号
     * @param passWord 密码
     * @return 如果登录成功返回 true，否则返回 false
     */
    private boolean loginAsAccount(Integer cardNumber, Integer passWord) {
        try {
            String sql = "SELECT * FROM accounts WHERE card_number = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cardNumber); // 设置第一个参数为卡号
            statement.setInt(2, passWord); // 设置第二个参数为密码
            ResultSet resultSet = statement.executeQuery(); // 执行查询
            return resultSet.next(); // 如果有匹配的行则返回 true
        } catch (SQLException e) {
            e.printStackTrace(); // 打印 SQL 异常的堆栈信息
            return false; // 发生异常时返回 false
        }
    }

    /**
     * 尝试以管理员身份登录
     * @param cardNumber 管理员卡号
     * @param passWord 管理员密码
     * @return 如果登录成功返回 true，否则返回 false
     */
    private boolean loginAsAdmin(Integer cardNumber, Integer passWord) {
        try {
            String sql = "SELECT * FROM admins WHERE card_number = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, cardNumber); // 设置第一个参数为管理员卡号
            statement.setInt(2, passWord); // 设置第二个参数为管理员密码
            ResultSet resultSet = statement.executeQuery(); // 执行查询
            return resultSet.next(); // 如果有匹配的行则返回 true
        } catch (SQLException e) {
            e.printStackTrace(); // 打印 SQL 异常的堆栈信息
            return false; // 发生异常时返回 false
        }
    }


    public static void main(String[] args) {
        // 创建登录窗口对象
        LoginFrame loginFrame = new LoginFrame();
        // 设置登录窗口可见
        loginFrame.setVisible(true);
    }
}


