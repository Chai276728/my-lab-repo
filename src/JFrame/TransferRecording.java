package JFrame;

import dao.TransferDao;
import model.Transfer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransferRecording extends JFrame {
    private int cardNumber;  // 卡号
    private int passWord;    // 密码
    private JTable table;    // 表格

    public TransferRecording(int cardNumber, int passWord) {
        this.cardNumber = cardNumber;  // 初始化卡号
        this.passWord = passWord;      // 初始化密码

        // 初始化窗口
        initialize();  // 调用初始化方法
    }

    private void initialize() {
        setTitle("转账记录");  // 设置窗口标题为“转账记录”
        setSize(600, 400);  // 设置窗口大小为600x400像素
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // 设置关闭窗口时默认的操作为释放窗体资源
        setLocationRelativeTo(null);  // 将窗口位置设置为居中显示

        // 创建表格
        table = new JTable();  // 创建一个空的表格
        JScrollPane scrollPane = new JScrollPane(table);  // 创建带滚动条的面板，并将表格放入其中
        getContentPane().add(scrollPane, BorderLayout.CENTER);  // 将带滚动条的面板添加到窗口的中间位置

        // 查询并显示转账记录
        displayTransferRecords();  // 调用方法显示转账记录

        setVisible(true);  // 设置窗口可见
    }

    private void displayTransferRecords() {
        // 通过 TransferDao 查询数据库中的转账记录
        TransferDao transferDao = new TransferDao();  // 创建 TransferDao 对象
        List<Transfer> transfers = transferDao.getTransfersByCardNumber(cardNumber);  // 调用 TransferDao 的方法获取特定卡号的转账记录列表

        // 设置表格模型
        DefaultTableModel model = new DefaultTableModel();  // 创建默认表格模型
        model.addColumn("发送者卡号");  // 添加列名：“发送者卡号”
        model.addColumn("接收者卡号");  // 添加列名：“接收者卡号”
        model.addColumn("金额");  // 添加列名：“金额”

        // 遍历转账记录列表，将每条记录添加到表格模型中
        for (Transfer transfer : transfers) {
            model.addRow(new Object[]{transfer.getSenderCardNumber(), transfer.getReceiverCardNumber(), transfer.getAmount()});
        }
        table.setModel(model);  // 将设置好的表格模型应用到表格中
    }
}




