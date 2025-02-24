package com.deepscience.rpa.view;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.rpc.api.register.dto.RpaLoginBindDTO;
import com.deepscience.rpa.util.ConfigUtils;
import com.deepscience.rpa.util.ImageUtils;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.util.frame.FrameUtils;
import com.deepscience.rpa.util.frame.entity.LimitDocument;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.Optional;

/**
 * 登录界面
 * @author yangzhuo
 * @date 2025/2/8 19:42
 */
@Slf4j
@RequiredArgsConstructor
public class LoginDialog {

    /**
     * 默认显示文字
     */
    private static final String DEFAULT_MSG = "请登录账号, 绑定设备!";

    /**
     * 最大输入字符限制
     */
    private static final int MAX_INPUT = 20;

    /**
     * 标题
     */
    private final String title;
    
    /**
     * 主对话框
     */
    private JDialog mainDialog;

    /**
     * 组件可见性
     */
    private volatile boolean visible = true;

    /**
     * 消息label
     */
    @Getter
    private JLabel msgLabel;

    /**
     * 用户名输入框
     */
    private JTextField usernameField;

    /**
     * 密码输入框
     */
    private JPasswordField passwordField;

    public void run() {
        String finalTitle = StrUtil.isEmpty(title) ? "" : title;
        mainDialog = new JDialog();
        mainDialog.setSize(320, 210);
        mainDialog.setLocationRelativeTo(null);
        mainDialog.setResizable(false);
        mainDialog.setAlwaysOnTop(true);
        mainDialog.setTitle(finalTitle);
        // 设置窗口图标
        ImageIcon icon = new ImageIcon(ImageUtils.getImage("images/company.png"));
        mainDialog.setIconImage(icon.getImage());

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

        mainDialog.add(mainPanel);
        mainDialog.setVisible(this.visible);

        // mainDialog 关闭事件监听
        mainDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                        .ifPresent(MainFrame::close);
            }
        });
    }


    /**
     * 创建文本框标题
     */
    private void createTitle(JPanel mainPanel) {
        // 提示信息
        JPanel titlePanel = new JPanel();
        // 输入平台账号 和 密码
        JLabel title = new JLabel("平台账号登录");
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

        // 设置用户名
        String defaultUsername = ConfigUtils.getUsername();
        if (StrUtil.isNotEmpty(defaultUsername)) {
            usernameField.setText(defaultUsername);
        }

        JLabel usernameLabel = new JLabel("用户名: ");
        usernamePanel.add(usernameLabel);
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
        start.setFocusPainted(false);
        start.addActionListener(event -> {
            login();
        });

        JButton end = new JButton("重置");
        end.setFocusPainted(false);
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

        try {
            LoginBindService loginBindService = SpringUtil.getBean(LoginBindService.class);
            String version = SpringUtil.getProperty("spring.application.version", "unknown");
            RpaLoginBindDTO rpaLoginBindDTO = RpaLoginBindDTO.builder()
                    .username(username)
                    .password(password)
                    .version(version)
                    .build();

            if (loginBindService.loginAndBind(rpaLoginBindDTO)) {
                MsgUtils.writeInfoMsg(msgLabel, "绑定成功!");
            } else {
                MsgUtils.writeErrorMsg(msgLabel, "绑定失败, 请检查账号密码!");
                return;
            }
        } catch (Exception e) {
            log.error("调用登录绑定服务失败", e);
            MsgUtils.writeErrorMsg(msgLabel, ExceptionUtil.getMessage(e));
            return;
        }

        // 登录绑定成功, 显示主界面
        Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                .ifPresent(mainDialog -> {
                    // 隐藏登录界面
                    setVisible(false);
                    // 显示主界面
                    mainDialog.setVisible(true);
                });
    }

    private void reset() {
        Optional.ofNullable(usernameField).ifPresent(field -> field.setText(""));
        Optional.ofNullable(passwordField).ifPresent(field -> field.setText(""));
    }

    public void resetMsg() {
        String msg = Optional.ofNullable(msgLabel).map(JLabel::getText).orElse(null);
        if (!DEFAULT_MSG.equals(msg)) {
            MsgUtils.writeInfoMsg(msgLabel, DEFAULT_MSG);
        }
    }

    /**
     * 设置登录对话框显示状态
     */
    public synchronized void setVisible(boolean visible) {
        this.visible = visible;
        if (Objects.nonNull(mainDialog)) {
            mainDialog.setVisible(visible);
        }
    }

}
