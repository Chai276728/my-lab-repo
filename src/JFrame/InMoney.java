package JFrame;

import dao.AccountDao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class InMoney extends JFrame {

    private int cardNumber; // 卡号
    private int passWord; // 密码
    private double currentBalance; // 当前余额

    private JLabel lblCurrentBalance; // 显示当前余额的标签
    private JTextField txtCurrentBalance; // 显示当前余额的文本框
    private JTextField txtDepositAmount; // 输入存款金额的文本框

    private AccountDao accountDao; // 数据访问对象，用于处理账户信息的访问

    public InMoney(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;
        this.passWord = passWord;
        this.accountDao = new AccountDao(); // 初始化 AccountDao

        initialize(); // 初始化界面
        fetchCurrentBalance(); // 获取并显示当前余额
    }

    private void initialize() {
        setTitle("存款"); // 设置窗口标题为"存款"
        setSize(700, 605); // 设置窗口大小为宽度700像素，高度605像素
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置关闭操作为释放窗口资源
        setLocationRelativeTo(null); // 将窗口位置设置为居中

        setLayout(new GridBagLayout()); // 使用网格包布局管理器
        GridBagConstraints gbc = new GridBagConstraints(); // 创建网格包约束

        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的外部填充

        lblCurrentBalance = new JLabel("当前余额: "); // 创建显示当前余额的标签
        Font labelFont = new Font("宋体", Font.PLAIN, 29); // 创建宋体字体，大小为29
        lblCurrentBalance.setFont(labelFont); // 应用字体设置
        gbc.gridx = 0; // 组件在网格中的列索引为0
        gbc.gridy = 0; // 组件在网格中的行索引为0
        gbc.anchor = GridBagConstraints.WEST; // 组件左对齐
        add(lblCurrentBalance, gbc); // 将标签添加到窗口

        txtCurrentBalance = new JTextField(18); // 创建18个字符宽度的文本框用于显示当前余额
        txtCurrentBalance.setEditable(false); // 设置文本框不可编辑
        txtCurrentBalance.setPreferredSize(new Dimension(txtCurrentBalance.getPreferredSize().width, txtCurrentBalance.getPreferredSize().height + 10)); // 增加文本框高度
        gbc.gridx = 1; // 组件在网格中的列索引为1
        gbc.gridy = 0; // 组件在网格中的行索引为0
        gbc.anchor = GridBagConstraints.WEST; // 组件左对齐
        add(txtCurrentBalance, gbc); // 将文本框添加到窗口

        JPanel panelDeposit = new JPanel(new GridBagLayout()); // 创建一个新的面板用于存款相关组件
        gbc.gridx = 0; // 组件在网格中的列索引为0
        gbc.gridy = 1; // 组件在网格中的行索引为1
        gbc.gridwidth = 2; // 组件跨越两列
        gbc.anchor = GridBagConstraints.WEST; // 组件左对齐
        add(panelDeposit, gbc); // 将存款面板添加到窗口

        txtDepositAmount = new JTextField(18); // 创建18个字符宽度的文本框用于输入存款金额
        txtDepositAmount.setPreferredSize(new Dimension(txtDepositAmount.getPreferredSize().width, txtDepositAmount.getPreferredSize().height + 10)); // 增加文本框高度
        JButton btnDeposit = new JButton("存款"); // 创建名为"存款"的按钮
        Font buttonFont = new Font("宋体", Font.PLAIN, 29); // 创建宋体字体，大小为29
        btnDeposit.setFont(buttonFont); // 应用字体设置
        btnDeposit.setPreferredSize(new Dimension(200, 50)); // 设置按钮大小为200x50像素

        GridBagConstraints gbcPanel = new GridBagConstraints(); // 创建存款面板的网格包约束
        gbcPanel.insets = new Insets(3, 3, 3, 3); // 设置存款面板组件之间的外部填充

        JLabel lblDepositAmount = new JLabel("存款金额: "); // 创建显示"存款金额"的标签
        lblDepositAmount.setFont(labelFont); // 设置宋体字体，大小为29
        gbcPanel.gridx = 0; // 组件在网格中的列索引为0
        gbcPanel.gridy = 0; // 组件在网格中的行索引为0
        gbcPanel.anchor = GridBagConstraints.WEST; // 组件左对齐
        panelDeposit.add(lblDepositAmount, gbcPanel); // 将标签添加到存款面板

        gbcPanel.gridx = 1; // 组件在网格中的列索引为1
        gbcPanel.gridy = 0; // 组件在网格中的行索引为0
        panelDeposit.add(txtDepositAmount, gbcPanel); // 将存款金额文本框添加到存款面板

        gbcPanel.gridx = 0; // 组件在网格中的列索引为0
        gbcPanel.gridy = 1; // 组件在网格中的行索引为1
        gbcPanel.gridwidth = 2; // 组件跨越两列
        gbcPanel.anchor = GridBagConstraints.CENTER; // 组件居中对齐
        panelDeposit.add(btnDeposit, gbcPanel); // 将存款按钮添加到存款面板

        btnDeposit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depositMoney(); // 监听存款按钮的动作事件
            }
        });

        pack(); // 根据窗口内容的大小调整窗口大小（可选）
    }

    // 获取当前余额并显示在界面上
    private void fetchCurrentBalance() {
        try {
            // 调用账户数据访问对象从数据库中获取指定卡号的账户余额
            currentBalance = accountDao.getAccountBalance(cardNumber);
            // 将获取到的当前余额显示在界面的文本框中
            txtCurrentBalance.setText(String.valueOf(currentBalance));
        } catch (SQLException ex) {
            ex.printStackTrace();
            // 捕获数据库异常并显示错误消息对话框
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 处理存款操作
    private void depositMoney() {
        // 获取用户输入的存款金额并去除首尾空格
        String depositAmountStr = txtDepositAmount.getText().trim();
        // 如果存款金额为空，显示错误消息对话框并返回
        if (depositAmountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入存款金额", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 将存款金额字符串转换为双精度浮点数
            double depositAmount = Double.parseDouble(depositAmountStr);
            // 如果存款金额小于等于零，显示错误消息对话框并返回
            if (depositAmount <= 0) {
                JOptionPane.showMessageDialog(this, "存款金额必须大于零", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 调用账户数据访问对象执行存款操作，传入卡号和存款金额
            if (accountDao.depositMoney(cardNumber, depositAmount)) {
                // 如果存款成功，更新当前余额并显示在界面的文本框中
                currentBalance += depositAmount;
                txtCurrentBalance.setText(String.valueOf(currentBalance));
                JOptionPane.showMessageDialog(this, "存款成功！");
            } else {
                // 如果存款失败，显示错误消息对话框
                JOptionPane.showMessageDialog(this, "存款失败，请重试", "错误", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            // 捕获存款金额格式错误异常，显示错误消息对话框
            JOptionPane.showMessageDialog(this, "存款金额格式错误", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            // 捕获数据库异常，显示错误消息对话框
            JOptionPane.showMessageDialog(this, "数据库错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InMoney inMoney = new InMoney(123456, 1234); // 示例账户号和密码
                inMoney.setVisible(true);
            }
        });
    }
}









