package com.deepscience.rpa;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.util.frame.FrameUtils;
import com.deepscience.rpa.util.frame.entity.LimitDocument;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class FrameTest {

    /**
     * 默认显示文字
     */
    private static final String DEFAULT_MSG = "请登录账号, 绑定设备!";

    /**
     * 最大输入字符限制
     */
    private static final int MAX_INPUT = 20;

    private JLabel msgLabel;

    private JTextField usernameField;

    private JPasswordField passwordField;

    @Test
    public void createLoginFrame() {
        JFrame loginFrame = new JFrame("绑定设备");
        loginFrame.setSize(320, 210);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.setAlwaysOnTop(true);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主界面
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 创建标题
        createTitle(mainPanel);

        // 用户名
        createUserName(mainPanel);

        // 密码
        createPassword(mainPanel);

        // 消息面板
        msgLabel = FrameUtils.createMsgPanel(mainPanel, "请登录账号, 绑定设备!",
                165, 20);

        // 登录/重置按钮
        createFunctionButton(mainPanel);

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);

        ThreadUtil.sleep(100000);
    }

    /**
     * 创建文本框标题
     */
    private void createTitle(JPanel mainPanel) {
        // 提示信息
        JPanel titlePanel = new JPanel();
        // 输入平台账号 和 密码
        JLabel title = new JLabel("全能智播账号登录");
        // 加黑粗体
        Font defaultFont = title.getFont();
        title.setFont(defaultFont.deriveFont(Font.BOLD, 15));
        titlePanel.add(title);
        mainPanel.add(titlePanel);
    }

    /**
     * 创建用户名
     */
    private void createUserName(JPanel mainPanel) {
        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 设置缩进
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        // 创建输入框
        usernameField = new JTextField(20);
        // 限制字符数量
        usernameField.setDocument(new LimitDocument(MAX_INPUT, this::resetMsg, () -> {
            MsgUtils.writeErrorMsg(msgLabel, StrUtil.format("用户名不能超过{}个字符!", MAX_INPUT));
        }));
        // 设置边框
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JLabel username = new JLabel("用户名: ");
        usernamePanel.add(username);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);
    }

    /**
     * 创建密码
     */
    private void createPassword(JPanel mainPanel) {
        JPanel passwordPanel = new JPanel();
        // 设置高度
        passwordPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 设置缩进
        passwordPanel.setBorder(BorderFactory.createEmptyBorder(0, 26, 0, 0));
        passwordField = new JPasswordField(20);
        // 限制字符数量
        passwordField.setDocument(new LimitDocument(MAX_INPUT, this::resetMsg, () -> {
            MsgUtils.writeErrorMsg(msgLabel, StrUtil.format("密码不能超过{}个字符!", MAX_INPUT));
        }));
        // 设置边框
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // 密码输入框用*号显示
        passwordField.setEchoChar('*');
        JLabel password = new JLabel("密码: ");
        passwordPanel.add(password);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);
    }

    /**
     * 创建主功能按钮
     */
    private void createFunctionButton(JPanel mainPanel) {
        // 创建按钮
        JButton start = new JButton("登录");
        start.addActionListener(event -> {
            login();
        });

        JButton end = new JButton("重置");
        end.addActionListener(event -> {
            reset();
        });
        // 创建按钮并按行手动添加
        JPanel rowPanel = new JPanel();
        rowPanel.add(start);
        rowPanel.add(end);
        mainPanel.add(rowPanel);
    }


    private void login() {
        String username = Optional.ofNullable(usernameField).map(JTextField::getText).orElse(null);
        String password = Optional.ofNullable(passwordField).map(JPasswordField::getPassword).map(String::new).orElse(null);
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)) {
            return;
        }

    }

    private void reset() {
        Optional.ofNullable(usernameField).ifPresent(field -> field.setText(""));
        Optional.ofNullable(passwordField).ifPresent(field -> field.setText(""));
    }

    private void resetMsg() {
        String msg = Optional.ofNullable(msgLabel).map(JLabel::getText).orElse(null);
        if (!DEFAULT_MSG.equals(msg)) {
            MsgUtils.writeInfoMsg(msgLabel, DEFAULT_MSG);
        }
    }

}
