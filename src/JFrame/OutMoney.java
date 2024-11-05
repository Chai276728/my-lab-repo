package JFrame;

import dao.AccountDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OutMoney extends JFrame {

    private int cardNumber; // 卡号
    private int passWord; // 密码
    private double balance; // 余额

    private JLabel lblBalance; // 余额标签
    private JLabel lblAmount; // 金额标签
    private JTextField txtAmount; // 金额输入框
    private JButton btnWithdraw; // 取款按钮
    private JTextField txtBalance; // 余额文本框

    private AccountDao accountDao; // 账户数据访问对象

    public OutMoney(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;
        this.passWord = passWord;

        // 初始化AccountDao
        accountDao = new AccountDao();

        // 初始化窗口
        initialize();
    }

    private void initialize() {
        setTitle("取款"); // 设置窗口标题为“取款”
        setSize(500, 400); // 设置窗口大小为500x400像素
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置关闭操作为释放窗口资源
        setLocationRelativeTo(null); // 将窗口位置设置为居中显示

        // 使用GridLayout进行布局，3行1列
        setLayout(new GridLayout(3, 1, 0, 10)); // 0表示水平和垂直之间的间隔，10表示垂直间隔为10像素

        // 添加当前余额标签和文本框
        JPanel balancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 左对齐
        lblBalance = new JLabel("当前余额：");
        lblBalance.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置标签字体为宋体，字体大小为29
        balancePanel.add(lblBalance);
        txtBalance = new JTextField(10); // 添加余额文本框
        txtBalance.setEditable(false); // 设置为不可编辑
        txtBalance.setPreferredSize(new Dimension(txtBalance.getPreferredSize().width, txtBalance.getPreferredSize().height + 10)); // 增加文本框高度
        balancePanel.add(txtBalance);
        add(balancePanel); // 将余额面板添加到窗口

        // 添加取款金额标签和输入框
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // 左对齐
        lblAmount = new JLabel("取款金额：");
        lblAmount.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置标签字体为宋体，字体大小为29
        amountPanel.add(lblAmount);
        txtAmount = new JTextField(10);
        txtAmount.setPreferredSize(new Dimension(txtAmount.getPreferredSize().width, txtAmount.getPreferredSize().height + 10)); // 增加文本框高度
        amountPanel.add(txtAmount);
        add(amountPanel); // 将取款金额面板添加到窗口

        // 添加取款按钮
        btnWithdraw = new JButton("取款");
        btnWithdraw.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnWithdraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                withdrawMoney(); // 调用取款方法
            }
        });
        add(btnWithdraw); // 将取款按钮添加到窗口

        // 初始化显示账户余额
        updateAccountBalance(); // 更新账户余额显示
    }

    private void updateAccountBalance() {
        try {
            balance = accountDao.getAccountBalance(cardNumber); // 从数据库中获取账户余额
            txtBalance.setText(String.valueOf(balance)); // 更新余额文本框显示
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法获取账户余额", "错误", JOptionPane.ERROR_MESSAGE); // 弹出错误提示框
        }
    }

    private void withdrawMoney() {
        // 获取用户输入的取款金额并去除首尾空格
        String amountStr = txtAmount.getText().trim();
        // 如果取款金额为空，显示错误消息对话框并返回
        if (amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入取款金额", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 将取款金额字符串转换为双精度浮点数
            double amount = Double.parseDouble(amountStr);

            // 如果取款金额不是100的倍数，显示错误消息对话框并返回
            if (amount % 100 != 0) {
                JOptionPane.showMessageDialog(this, "取款金额必须为100的倍数", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 如果单次取款金额超过5000元，显示错误消息对话框并返回
            if (amount > 5000) {
                JOptionPane.showMessageDialog(this, "单次取款金额不能超过5000元", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 如果取款金额大于账户余额，显示错误消息对话框并返回
            if (amount > balance) {
                JOptionPane.showMessageDialog(this, "账户余额不足", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 计算新的余额并更新数据库中的账户余额
            double newBalance = balance - amount;
            boolean updated = accountDao.updateAccountBalance(cardNumber, newBalance);

            // 如果更新成功，更新界面上的余额显示并显示取款成功消息对话框
            if (updated) {
                txtBalance.setText(String.valueOf(newBalance)); // 更新余额文本框显示
                JOptionPane.showMessageDialog(this, "取款成功");
                balance = newBalance; // 更新当前类中的余额变量
                txtAmount.setText(""); // 清空取款金额输入框
            } else {
                // 如果更新失败，显示取款失败消息对话框
                JOptionPane.showMessageDialog(this, "取款失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            // 捕获取款金额格式错误异常，显示错误消息对话框
            JOptionPane.showMessageDialog(this, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            // 捕获数据库异常，显示错误消息对话框并打印异常信息到控制台
            JOptionPane.showMessageDialog(this, "数据库错误，请稍后再试", "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 示例用法：创建取款界面
        int cardNumber = 12345678; // 替换为实际卡号
        int passWord = 1234; // 替换为实际密码

        OutMoney outMoney = new OutMoney(cardNumber, passWord);
        outMoney.setVisible(true);
    }
}



