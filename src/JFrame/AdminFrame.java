package JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 管理员界面的主窗体类
public class AdminFrame extends JFrame {
    private JDesktopPane desktopPane; // 桌面面板，用于放置内部窗体

    // 构造方法
    public AdminFrame() {
        setTitle("管理员界面"); // 设置窗体标题
        setSize(1200, 800); // 设置窗体大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作为退出程序

        // 创建菜单栏并设置字体
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置菜单栏字体
        setJMenuBar(menuBar); // 将菜单栏设置为窗体的菜单栏

        // 创建个人信息管理菜单并设置字体
        JMenu userInfoMenu = new JMenu("账户信息管理");
        userInfoMenu.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置菜单项字体
        menuBar.add(userInfoMenu); // 将个人信息管理菜单添加到菜单栏中

        // 创建查看账户信息菜单项并设置字体和动作监听器
        JMenuItem manageUserInfoItem = new JMenuItem("查看账户信息");
        manageUserInfoItem.setFont(new Font("宋体", Font.PLAIN, 29)); // 设置菜单项字体
        manageUserInfoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 创建账户信息管理窗体并显示
                AdminAccountInfoManager adminAccountInfoManager = new AdminAccountInfoManager();
                desktopPane.add(adminAccountInfoManager); // 将内部窗体添加到桌面面板
                adminAccountInfoManager.setVisible(true); // 显示内部窗体
            }
        });
        userInfoMenu.add(manageUserInfoItem); // 将查看账户信息菜单项添加到个人信息管理菜单中

        // 创建桌面面板并设置为主窗体的内容面板
        desktopPane = new JDesktopPane();
        setContentPane(desktopPane); // 设置桌面面板为主窗体的内容面板

        // 添加背景图片
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource("/resources/zgyhht.jpg"));
        Image image = backgroundImage.getImage().getScaledInstance(900, 800, Image.SCALE_DEFAULT);
        ImageIcon scaledBackgroundImage = new ImageIcon(image);
        JLabel backgroundLabel = new JLabel(scaledBackgroundImage);
        backgroundLabel.setBounds(0, 0, 1200, 700); // 设置背景标签的位置和大小

        desktopPane.add(backgroundLabel, new Integer(Integer.MIN_VALUE)); // 将背景标签添加到桌面面板最底层

        desktopPane.setLayout(new BorderLayout()); // 设置桌面面板的布局为边界布局
    }

    // 主方法，程序入口
    public static void main(String[] args) {
        // 使用SwingUtilities.invokeLater()来确保在事件调度线程上创建和显示窗体
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminFrame adminFrm = new AdminFrame(); // 创建管理员界面主窗体实例
                adminFrm.setVisible(true); // 设置窗体可见
            }
        });
    }
}






