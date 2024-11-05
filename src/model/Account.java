package model;

public class Account {
    // 成员变量
    private Integer cardNumber; // 卡号
    private String userName; // 用户名
    private Integer passWord; // 密码
    private double balance; // 余额

    // 构造函数，初始金额为0
    public Account(Integer cardNumber, String userName, Integer passWord) {
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.passWord = passWord;
        this.balance = 0; // 初始金额为0
    }

    // 空构造函数，初始化为空值
    public Account() {
        this.cardNumber = 0;
        this.userName = "";
        this.passWord = 0;
        this.balance = 0.0;
    }

    // 构造函数，接受卡号、用户名、密码和金额
    public Account(int cardNumber, String username, int password, double money) {
        this.cardNumber = cardNumber;
        this.userName = username;
        this.passWord = password;
        this.balance = money;
    }

    // 构造函数，接受卡号和余额
    public Account(int cardNumber, double balance) {
        this.cardNumber = cardNumber;
        this.balance = balance;
    }

    // 获取卡号
    public Integer getCardNumber() {
        return cardNumber;
    }

    // 设置卡号
    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    // 获取用户名
    public String getUserName() {
        return userName;
    }

    // 设置用户名
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 获取密码
    public Integer getPassWord() {
        return passWord;
    }

    // 设置密码
    public void setPassWord(Integer passWord) {
        this.passWord = passWord;
    }

    // 获取余额
    public double getBalance() {
        return balance;
    }

    // 设置余额
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // 存款操作，增加指定金额到余额
    public void deposit(double amount) {
        balance += amount;
    }

    // 取款操作，减少指定金额从余额
    public void withdraw(double amount) {
        balance -= amount;
    }
}

