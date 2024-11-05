package JFrame;

import dao.TransferDao;
import model.Transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferMoney extends JFrame {

    private int cardNumber; // 卡号
    private int passWord; // 密码

    private JLabel senderLabel; // 发送者标签
    private JLabel cardNumberLabel; // 卡号
    private JTextField receiverField; // 收款字段
    private JTextField amountField; // 金额
    private JButton transferButton; // 转账按钮

    private TransferDao transferDao; // 转账数据访问对象

    public TransferMoney(int cardNumber, int passWord) {
        this.cardNumber = cardNumber; // 设置银行卡号
        this.passWord = passWord; // 设置密码
        this.transferDao = new TransferDao(); // 初始化转账数据访问对象

        initialize(); // 初始化界面
        addComponents(); // 添加组件
        setVisible(true); // 设置界面可见
    }

    private void initialize() {
        setTitle("转账"); // 设置窗口标题为“转账”
        setSize(500, 400); // 设置窗口大小为500x400像素
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 设置默认关闭操作为关闭窗口
        setLocationRelativeTo(null); // 将窗口位置设置为屏幕中央
        setLayout(new GridBagLayout()); // 使用网格包布局管理器

        senderLabel = new JLabel("当前账号: "); // 创建发送者账号标签
        cardNumberLabel = new JLabel("收款账号: "); // 创建收款账号标签
        receiverField = new JTextField(10); // 创建收款账号文本框，宽度为10个字符
        amountField = new JTextField(10); // 创建金额文本框，宽度为10个字符
        transferButton = new JButton("转账"); // 创建转账按钮

        Font labelFont = new Font("宋体", Font.PLAIN, 29); // 创建宋体字体，普通样式，大小29的字体对象
        Font buttonFont = new Font("宋体", Font.PLAIN, 29); // 创建宋体字体，普通样式，大小29的字体对象

        senderLabel.setFont(labelFont); // 设置发送者账号标签字体
        cardNumberLabel.setFont(labelFont); // 设置收款账号标签字体
        transferButton.setFont(buttonFont); // 设置转账按钮字体

        // 添加转账按钮的动作监听器，点击时调用handleTransfer方法
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTransfer();
            }
        });
    }

    private void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;  // 组件在网格中的横坐标，初始为0
        constraints.gridy = 0;  // 组件在网格中的纵坐标，初始为0
        constraints.anchor = GridBagConstraints.WEST;  // 组件在其显示区域内的位置，初始为左对齐
        constraints.insets = new Insets(10, 10, 10, 10);  // 组件与周围的空白距离，上、左、下、右分别为10像素

        // 当前账号标签
        senderLabel.setFont(new Font("宋体", Font.PLAIN, 29));  // 设置标签字体为宋体，大小为29
        add(senderLabel, constraints);  // 将senderLabel标签添加到窗口中

        // 当前账号数据标签
        constraints.gridx = 1;  // 横坐标变为1
        JLabel cardNumberDataLabel = new JLabel(String.valueOf(cardNumber));  // 创建一个显示cardNumber的标签
        cardNumberDataLabel.setFont(new Font("宋体", Font.PLAIN, 29));  // 设置标签字体为宋体，大小为29
        add(cardNumberDataLabel, constraints);  // 将cardNumberDataLabel标签添加到窗口中

        // 收款账号标签
        constraints.gridy = 1;  // 纵坐标变为1
        constraints.gridx = 0;  // 横坐标重新设为0
        cardNumberLabel.setFont(new Font("宋体", Font.PLAIN, 29));  // 设置标签字体为宋体，大小为29
        add(cardNumberLabel, constraints);  // 将cardNumberLabel标签添加到窗口中

        // 收款账号文本框
        constraints.gridx = 1;  // 横坐标变为1
        receiverField.setPreferredSize(new Dimension(receiverField.getPreferredSize().width, receiverField.getPreferredSize().height + 10));  // 增加文本框高度
        add(receiverField, constraints);  // 将receiverField文本框添加到窗口中

        // 转账金额标签
        constraints.gridy = 2;  // 纵坐标变为2
        constraints.gridx = 0;  // 横坐标重新设为0
        JLabel amountLabel = new JLabel("转账金额: ");  // 创建一个显示“转账金额: ”的标签
        amountLabel.setFont(new Font("宋体", Font.PLAIN, 29));  // 设置标签字体为宋体，大小为29
        add(amountLabel, constraints);  // 将amountLabel标签添加到窗口中

        // 转账金额文本框
        constraints.gridx = 1;  // 横坐标变为1
        amountField.setPreferredSize(new Dimension(amountField.getPreferredSize().width, amountField.getPreferredSize().height + 10));  // 增加文本框高度
        add(amountField, constraints);  // 将amountField文本框添加到窗口中

        // 转账按钮
        constraints.gridy = 3;  // 纵坐标变为3
        constraints.gridx = 0;  // 横坐标重新设为0
        constraints.gridwidth = 2;  // 组件横跨2个网格
        constraints.anchor = GridBagConstraints.CENTER;  // 组件在其显示区域内居中对齐
        transferButton.setPreferredSize(new Dimension(200, 50));  // 设置按钮大小为200x50像素
        add(transferButton, constraints);  // 将transferButton按钮添加到窗口中
    }

    private void handleTransfer() {
        // 获取收款账号和转账金额文本框中的内容，并去除首尾的空格
        String receiverAccountStr = receiverField.getText().trim();
        String amountStr = amountField.getText().trim();

        // 检查是否输入了收款账号和转账金额
        if (receiverAccountStr.isEmpty() || amountStr.isEmpty()) {
            // 如果有任一为空，弹出错误提示框
            JOptionPane.showMessageDialog(this, "请输入收款账号和转账金额！", "错误", JOptionPane.ERROR_MESSAGE);
            return;  // 结束方法
        }

        Integer receiverCardNumber;
        double amount;

        try {
            // 尝试将收款账号和转账金额转换为合适的数据类型
            receiverCardNumber = Integer.valueOf(receiverAccountStr);  // 将收款账号转换为整数
            amount = Double.parseDouble(amountStr);  // 将转账金额转换为双精度浮点数
        } catch (NumberFormatException e) {
            // 捕获转换异常，说明输入的格式不正确，弹出错误提示框
            JOptionPane.showMessageDialog(this, "收款账号或转账金额格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
            return;  // 结束方法
        }

        // 创建一个Transfer对象，表示要进行的转账操作
        Transfer transfer = new Transfer(cardNumber, receiverCardNumber, amount);

        // 调用transferDao的transfer方法执行转账操作，并接收操作结果
        boolean success = transferDao.transfer(cardNumber, receiverCardNumber, amount);

        if (success) {
            // 如果转账成功
            boolean insertSuccess = transferDao.recordTransfer(transfer);  // 记录转账信息
            if (insertSuccess) {
                // 如果记录转账信息成功，弹出转账成功的信息提示框
                JOptionPane.showMessageDialog(this, "转账成功，转账信息已记录！", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 可以在这里进行界面更新或者其他操作
            } else {
                // 如果记录转账信息失败，弹出警告提示框
                JOptionPane.showMessageDialog(this, "转账成功，但转账信息记录失败！", "警告", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            // 如果转账失败，弹出失败提示框
            JOptionPane.showMessageDialog(this, "转账失败，请检查输入信息！", "失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // 示例用法
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TransferMoney transferMoney = new TransferMoney(123456, 1234);
            }
        });
    }
}





