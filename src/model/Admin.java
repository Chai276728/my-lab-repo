package model;

public class Admin {
    // 成员变量
    private Integer cardNumber; // 管理员卡号
    private String userName; // 管理员用户名
    private Integer passWord; // 管理员密码

    // 构造函数，接受管理员卡号、用户名和密码作为参数
    public Admin(Integer cardNumber, String userName, Integer passWord) {
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.passWord = passWord;
    }

    // 空构造函数
    public Admin() {
    }

    // 获取管理员卡号
    public Integer getCardNumber() {
        return cardNumber;
    }

    // 设置管理员卡号
    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    // 获取管理员用户名
    public String getUserName() {
        return userName;
    }

    // 设置管理员用户名
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // 获取管理员密码
    public Integer getPassWord() {
        return passWord;
    }

    // 设置管理员密码
    public void setPassWord(Integer passWord) {
        this.passWord = passWord;
    }
}

