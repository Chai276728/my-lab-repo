package JFrame;

import dao.AccountDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangePassword extends JFrame {

    private int cardNumber; // 账号
    private int passWord;   // 当前密码，这里假设为整数，实际中可能需要更安全的方式存储

    private JLabel accountLabel; // 显示账号的标签
    private JLabel cardNumberLabel; // 显示账号数据的标签
    private JPasswordField oldPasswordField; // 旧密码输入框
    private JPasswordField newPasswordField; // 新密码输入框
    private JPasswordField confirmNewPasswordField; // 确认新密码输入框
    private JButton changeButton; // 修改密码按钮

    public ChangePassword(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;
        this.passWord = passWord;

        initialize(); // 窗口初始化
        addComponents(); // 添加界面组件
        setVisible(true); // 显示窗口
    }

    private void initialize() {
        setTitle("修改密码");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中

        // 使用GridBagLayout布局管理器
        setLayout(new GridBagLayout());
    }

    private void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 10, 5, 10); // 设置组件之间的间距

        // 添加账号标签
        constraints.gridx = 0;
        constraints.gridy = 0;
        accountLabel = new JLabel("账号：");
        add(accountLabel, constraints);

        // 显示账号数据的标签
        constraints.gridx = 1;
        cardNumberLabel = new JLabel(String.valueOf(cardNumber));
        add(cardNumberLabel, constraints);

        // 添加旧密码输入框
        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel oldPasswordLabel = new JLabel("旧密码:");
        add(oldPasswordLabel, constraints);

        constraints.gridx = 1;
        oldPasswordField = new JPasswordField(15);
        add(oldPasswordField, constraints);

        // 添加新密码输入框
        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel newPasswordLabel = new JLabel("新密码:");
        add(newPasswordLabel, constraints);

        constraints.gridx = 1;
        newPasswordField = new JPasswordField(15);
        add(newPasswordField, constraints);

        // 添加确认新密码输入框
        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel confirmNewLabel = new JLabel("确认新密码:");
        add(confirmNewLabel, constraints);

        constraints.gridx = 1;
        confirmNewPasswordField = new JPasswordField(15);
        add(confirmNewPasswordField, constraints);

        // 添加修改密码按钮
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2; // 指定按钮横跨两列
        constraints.fill = GridBagConstraints.HORIZONTAL; // 按钮水平填充
        changeButton = new JButton("修改密码");
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChangePassword(); // 设置修改密码按钮点击事件处理方法
            }
        });
        add(changeButton, constraints);
    }

    // 处理修改密码按钮点击事件
    private void handleChangePassword() {
        char[] oldPassword = oldPasswordField.getPassword(); // 获取旧密码输入
        char[] newPassword = newPasswordField.getPassword(); // 获取新密码输入
        char[] confirmNewPassword = confirmNewPasswordField.getPassword(); // 获取确认新密码输入

        if (oldPassword.length == 0 || newPassword.length == 0 || confirmNewPassword.length == 0) {
            JOptionPane.showMessageDialog(this, "请填写所有密码字段", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!verifyOldPassword(oldPassword)) {
            JOptionPane.showMessageDialog(this, "旧密码错误", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isNewPasswordValid(newPassword, confirmNewPassword)) {
            return; // 新密码验证失败，由isNewPasswordValid方法处理错误信息
        }

        if (updatePasswordInDatabase(newPassword)) {
            JOptionPane.showMessageDialog(this, "密码修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // 关闭窗口
        } else {
            JOptionPane.showMessageDialog(this, "密码修改失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 验证旧密码是否正确
    private boolean verifyOldPassword(char[] oldPassword) {
        // 简单比较输入的旧密码与当前密码
        return new String(oldPassword).equals(String.valueOf(this.passWord));
    }

    // 验证新密码是否符合要求
    private boolean isNewPasswordValid(char[] newPassword, char[] confirmNewPassword) {
        String newPasswordStr = new String(newPassword);
        String confirmNewPasswordStr = new String(confirmNewPassword);

        if (newPassword.length < 6) {
            JOptionPane.showMessageDialog(this, "新密码长度不能小于6位", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 检查新密码是否与旧密码相同
        if (isNewPasswordSameAsOld(newPasswordStr)) {
            JOptionPane.showMessageDialog(this, "新密码不能与旧密码相同", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 检查是否有6位完全相同的情况
        if (isPasswordAllSame(newPassword)) {
            JOptionPane.showMessageDialog(this, "新密码不能完全相同", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (newPasswordStr.equals(confirmNewPasswordStr)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "两次输入的新密码不一致", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // 检查新密码是否与旧密码相同
    private boolean isNewPasswordSameAsOld(String newPassword) {
        return newPassword.equals(String.valueOf(this.passWord));
    }

    // 检查新密码是否完全相同的辅助方法
    private boolean isPasswordAllSame(char[] password) {
        for (int i = 1; i < password.length; i++) {
            if (password[i] != password[0]) {
                return false;
            }
        }
        return true;
    }

    // 更新数据库中的密码
    private boolean updatePasswordInDatabase(char[] newPassword) {
        try {
            Connection connection = AccountDao.getConnection(); // 获取数据库连接
            String updateQuery = "UPDATE accounts SET password = ? WHERE card_number = ?"; // 更新密码的SQL语句
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, new String(newPassword)); // 设置新密码
            preparedStatement.setInt(2, cardNumber); // 设置账号

            int rowsUpdated = preparedStatement.executeUpdate(); // 执行更新操作
            return rowsUpdated > 0; // 如果更新行数大于0，则更新成功
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // 更新失败
        }
    }

    public static void main(String[] args) {
        // 示例用法
        SwingUtilities.invokeLater(() -> new ChangePassword(123456789, 123456));
    }
}



