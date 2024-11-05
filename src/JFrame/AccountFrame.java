package JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountFrame extends JFrame {

    private int cardNumber;
    private int passWord;
    private JPanel panelLeft;
    private JPanel panelRight;
    private JPanel panelCenter;

    // 保存其他窗口的引用
    private InMoney inMoneyWindow;//引用 InMoney 类型的窗口，用于处理存款操作。
    private OutMoney outMoneyWindow;
    private InquireMoney inquireMoneyWindow;
    private ChangePassword changePasswordWindow;
    private TransferMoney transferMoneyWindow;
    private TransferRecording transferRecordingWindow;

    public AccountFrame(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;
        this.passWord = passWord;

        setTitle("ATM操作界面");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置布局为边界布局
        setLayout(new BorderLayout());

        // 创建面板
        panelLeft = new JPanel();
        panelLeft.setLayout(new GridLayout(4, 1));

        panelRight = new JPanel();
        panelRight.setLayout(new GridLayout(3, 1));

        panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout());

        // 添加中间的图片，并调整大小
        ImageIcon imageIcon = new ImageIcon("src/resources/zgyhft.jpg");
        Image image = imageIcon.getImage(); // 将 ImageIcon 转换为 Image

        // 调整图片大小
        int width = 500; // 设置新的宽度
        int height = 450; // 设置新的高度
        Image newImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH); // 缩放图片

        // 创建调整大小后的 ImageIcon
        ImageIcon newImageIcon = new ImageIcon(newImage);
        JLabel imageLabel = new JLabel(newImageIcon);
        panelCenter.add(imageLabel);

        // 将面板添加到窗口中
        add(panelLeft, BorderLayout.WEST);
        add(panelRight, BorderLayout.EAST);
        add(panelCenter, BorderLayout.CENTER);

        // 添加左侧功能按钮
        // 添加存款按钮
        JButton btnDeposit = new JButton("存款");
        btnDeposit.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnDeposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击存款按钮时的事件处理
                if (inMoneyWindow == null || !inMoneyWindow.isVisible()) {
                    // 如果存款窗口不存在或不可见，创建一个新的存款窗口，并设置为可见
                    inMoneyWindow = new InMoney(cardNumber, passWord);
                    inMoneyWindow.setVisible(true);
                }
            }
        });
        panelLeft.add(btnDeposit); // 将存款按钮添加到左侧面板(panelLeft)中

        // 添加取款按钮，处理方式类似
        JButton btnWithdraw = new JButton("取款");
        btnWithdraw.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnWithdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击取款按钮时的事件处理
                if (outMoneyWindow == null || !outMoneyWindow.isVisible()) {
                    // 如果取款窗口不存在或不可见，创建一个新的取款窗口，并设置为可见
                    outMoneyWindow = new OutMoney(cardNumber, passWord);
                    outMoneyWindow.setVisible(true);
                }
            }
        });
        panelLeft.add(btnWithdraw); // 将取款按钮添加到左侧面板(panelLeft)中

        // 添加余额查询按钮，处理方式类似
        JButton btnInquire = new JButton("余额查询");
        btnInquire.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnInquire.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击余额查询按钮时的事件处理
                if (inquireMoneyWindow == null || !inquireMoneyWindow.isVisible()) {
                    // 如果余额查询窗口不存在或不可见，创建一个新的余额查询窗口，并设置为可见
                    inquireMoneyWindow = new InquireMoney(cardNumber, passWord);
                    inquireMoneyWindow.setVisible(true);
                }
            }
        });
        panelLeft.add(btnInquire); // 将余额查询按钮添加到左侧面板(panelLeft)中

        // 添加修改密码按钮，处理方式类似
        JButton btnChangePwd = new JButton("修改密码");
        btnChangePwd.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnChangePwd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击修改密码按钮时的事件处理
                if (changePasswordWindow == null || !changePasswordWindow.isVisible()) {
                    // 如果修改密码窗口不存在或不可见，创建一个新的修改密码窗口，并设置为可见
                    changePasswordWindow = new ChangePassword(cardNumber, passWord);
                    changePasswordWindow.setVisible(true);
                }
            }
        });
        panelLeft.add(btnChangePwd); // 将修改密码按钮添加到左侧面板(panelLeft)中

        // 添加右侧功能按钮
        // 添加转账按钮
        JButton btnTransfer = new JButton("转账");
        btnTransfer.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnTransfer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击转账按钮时的事件处理
                if (transferMoneyWindow == null || !transferMoneyWindow.isVisible()) {
                    // 如果转账窗口不存在或不可见，创建一个新的转账窗口，并设置为可见
                    transferMoneyWindow = new TransferMoney(cardNumber, passWord);
                    transferMoneyWindow.setVisible(true);
                }
            }
        });
        panelRight.add(btnTransfer); // 将转账按钮添加到右侧面板(panelRight)中

        // 添加交易记录按钮，处理方式类似
        JButton btnRecords = new JButton("交易记录");
        btnRecords.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnRecords.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击交易记录按钮时的事件处理
                if (transferRecordingWindow == null || !transferRecordingWindow.isVisible()) {
                    // 如果交易记录窗口不存在或不可见，创建一个新的交易记录窗口，并设置为可见
                    transferRecordingWindow = new TransferRecording(cardNumber, passWord);
                    transferRecordingWindow.setVisible(true);
                }
            }
        });
        panelRight.add(btnRecords); // 将交易记录按钮添加到右侧面板(panelRight)中

        // 添加退出按钮，处理方式类似
        JButton btnExit = new JButton("退出");
        btnExit.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置按钮字体为宋体，字体大小为29

        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击退出按钮时的事件处理
                // 关闭其他窗口
                if (inMoneyWindow != null) {
                    inMoneyWindow.dispose();
                }
                if (outMoneyWindow != null) {
                    outMoneyWindow.dispose();
                }
                if (inquireMoneyWindow != null) {
                    inquireMoneyWindow.dispose();
                }
                if (changePasswordWindow != null) {
                    changePasswordWindow.dispose();
                }
                if (transferMoneyWindow != null) {
                    transferMoneyWindow.dispose();
                }
                if (transferRecordingWindow != null) {
                    transferRecordingWindow.dispose();
                }

                // 关闭当前窗口
                dispose();
            }
        });
        panelRight.add(btnExit); // 将退出按钮添加到右侧面板(panelRight)中

    }

    public static void main(String[] args) {
        // 示例用法：创建账户界面
        int cardNumber = 12345678; // 替换为实际卡号
        int passWord = 1234; // 替换为实际密码

        AccountFrame accountFrame = new AccountFrame(cardNumber, passWord);
        accountFrame.setVisible(true);
    }
}


