package model;

public class Transfer {
    // 发送者卡号
    private Integer senderCardNumber;
    // 接收者卡号
    private Integer receiverCardNumber;
    // 转账金额
    private double amount;

    // 构造函数，用于初始化 Transfer 对象
    public Transfer(Integer senderCardNumber, Integer receiverCardNumber, double amount) {
        this.senderCardNumber = senderCardNumber;      // 设置发送者卡号
        this.receiverCardNumber = receiverCardNumber;  // 设置接收者卡号
        this.amount = amount;                          // 设置转账金额
    }

    // Getter 方法，用于获取私有字段的值

    // 获取发送者卡号
    public Integer getSenderCardNumber() {
        return senderCardNumber;
    }

    // 设置发送者卡号
    public void setSenderCardNumber(Integer senderCardNumber) {
        this.senderCardNumber = senderCardNumber;
    }

    // 获取接收者卡号
    public Integer getReceiverCardNumber() {
        return receiverCardNumber;
    }

    // 设置接收者卡号
    public void setReceiverCardNumber(Integer receiverCardNumber) {
        this.receiverCardNumber = receiverCardNumber;
    }

    // 获取转账金额
    public double getAmount() {
        return amount;
    }

    // 设置转账金额
    public void setAmount(double amount) {
        this.amount = amount;
    }
}


