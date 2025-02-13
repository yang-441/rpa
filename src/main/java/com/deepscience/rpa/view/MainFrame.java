package com.deepscience.rpa.view;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.config.properties.MainFrameProperties;
import com.deepscience.rpa.common.constants.HtmlTemplateConstants;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.model.version.service.VersionService;
import com.deepscience.rpa.rpc.api.version.dto.VersionInfoDTO;
import com.deepscience.rpa.util.ConfigUtils;
import com.deepscience.rpa.util.ImageUtils;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.util.frame.FrameUtils;
import com.deepscience.rpa.view.filter.LiveWorkbenchFilter;
import com.deepscience.rpa.view.listener.CloseEventListener;
import com.deepscience.rpa.view.listener.DebugEventListener;
import com.deepscience.rpa.view.listener.StartStopEventListener;
import com.deepscience.rpa.view.listener.UnbindEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

/**
 * 自动开播应用
 * @author yangzhuo
 * @date 2025/1/23 16:12
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MainFrame {
    /**
     * 窗口
     */
    private JFrame frame;

    /**
     * 登录对话框
     */
    private LoginDialog loginDialog;

    /**
     * 主框架属性
     */
    private final MainFrameProperties mainFrameProperties;

    /**
     * 文件过滤器
     */
    private final LiveWorkbenchFilter liveWorkbenchFilter;

    /**
     * 开始事件监听
     */
    private final DebugEventListener debugEventListener;

    /**
     * 解绑事件监听
     */
    private final UnbindEventListener unbindEventListener;

    /**
     * 停止事件监听
     */
    private final StartStopEventListener startStopEventListener;

    /**
     * 关闭事件监听
     */
    private final CloseEventListener closeEventListener;

    /**
     * 版本号服务
     */
    private final VersionService versionService;

    /**
     * 登录绑定服务
     */
    private final LoginBindService loginBindService;

    public void run() {
        // 运行窗口可见性
        boolean visible = true;
        // 登录窗口出现时, 关闭主窗口
        Optional.ofNullable(SpringUtil.getBean(MainFrame.class)).ifPresent(mainFrame -> mainFrame.setVisible(false));
        // debug模式, 添加一些调试元素
        boolean debugEnabled = log.isDebugEnabled();
        // 配置信息
        String version = SpringUtil.getProperty("spring.application.version", "unknown");
        String active = SpringUtil.getProperty("spring.profiles.active", "unknown");

        VersionInfoDTO versionInfo;
        try {
            // 校验版本号
            versionInfo = versionService.checkVersion(version);
            loginBindService.isValidBind();
        } catch (Exception e) {
            log.error("程序启动时, 接口调用失败", e);
            versionInfo = null;
        }

        // 是否强制更新
        boolean forceUpdate = Optional.ofNullable(versionInfo)
                .map(v -> Boolean.TRUE.equals(v.getForceUpdate()))
                .orElse(true);

        if (forceUpdate) {
            // 创建 JEditorPane 显示 HTML 内容
            JDialog editorPane = createDialog(versionInfo);
            // 退出程序
            System.exit(0);
        }

        // 应用标题
        String title = StrUtil.format("自动开播RPA工具 [{}_{}]{}", version, active, log.isDebugEnabled() ? "debug" : "");

        // 创建 JFrame 窗口
        frame = new JFrame(title);
        // 设置窗口图标
        ImageIcon icon = new ImageIcon(ImageUtils.getImagePath("images/company.png"));
        frame.setIconImage(icon.getImage());
        // 设置窗口置顶
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(mainFrameProperties.getWidth(), debugEnabled ? mainFrameProperties.getDebugHeight() : mainFrameProperties.getHeight());
        // 将窗口设置在屏幕中心
        frame.setLocationRelativeTo(null);
        // 创建主界面
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 创建登录绑定对话框
        String clientCredentials = ConfigUtils.getClientCredentials();
        String loginTitle = StrUtil.format("绑定设备 [{}_{}]{}", version, active, log.isDebugEnabled() ? "debug" : "");
        loginDialog = new LoginDialog(loginTitle);
        if (StrUtil.isEmpty(clientCredentials)) {
            loginDialog.setVisible(true);
            visible = false;
        } else {
            loginDialog.setVisible(false);
        }
        loginDialog.run();

        // 主窗口可见, 且工作台已配置
        if (visible && StrUtil.isNotEmpty(ConfigUtils.getWorkbenchLocation())) {
            VariableContainer.setRunningState(RunningStateEnum.RUNNING);
        }

        // 创建文件选择框
        createFileChooser(mainPanel);

        if (debugEnabled) {
            // 创建debug模式选择框
            createDebugFrame(mainPanel);
        }

        // 创建主功能按钮
        createMainFunctionButton(mainPanel);

        // 创建消息框
        JLabel msgLabel = FrameUtils.createMsgPanel(mainPanel, VariableContainer.isRunning() ? "运行中..." : "选择直播工作台，点击启动!", mainFrameProperties.getMsgWidth(), mainFrameProperties.getMsgHeight());
        VariableContainer.setMessageLabel(msgLabel);
        if (VariableContainer.isRunning()) {
            MsgUtils.writeSuccessMsg("运行中, 等待任务执行...");
        }

        // 将面板添加到框架
        frame.add(mainPanel);

        // 注册事件监听
        frame.addWindowListener(closeEventListener);

        // 设置窗口可见
        frame.setVisible(visible);
    }

    private static JDialog createDialog(VersionInfoDTO versionInfo) {
        String msg;
        int width;
        int height;
        if (Objects.isNull(versionInfo)) {
            msg = HtmlTemplateConstants.SERVER_ERROR_TEMPLATE;
            width = 360;
            height = 128;
        } else {
            if (StrUtil.isEmpty(versionInfo.getHtmlNotice())) {
                msg = StrUtil.format(HtmlTemplateConstants.UPDATE_NOTICE_TEMPLATE,
                        versionInfo.getVersion(), versionInfo.getLatestVersion(),
                        versionInfo.getNotice(), versionInfo.getDownloadUrl());
            } else {
                msg = versionInfo.getHtmlNotice();
            }
            width = 400;
            height = 280;
        }
        JEditorPane editorPane = new JEditorPane("text/html", msg);
        // 设置为不可编辑
        editorPane.setEditable(false);
        // 设置背景色为白色
        editorPane.setBackground(Color.WHITE);

        // 创建 JDialog 来显示消息框，并设置为始终置顶
        JDialog dialog = new JDialog();
        dialog.setTitle("版本校验");
        // 设置为模态对话框
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        // 设置对话框总是显示在最上层
        dialog.setAlwaysOnTop(true);
        // 禁止调整大小
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 将 JEditorPane 放入滚动面板中，防止内容过长时出现滚动条
        JScrollPane scrollPane = new JScrollPane(editorPane);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // 为超链接添加点击事件
        editorPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                try {
                    // 打开链接
                    URI uri = new URI(e.getURL().toString());
                    // 使用默认浏览器打开链接
                    Desktop.getDesktop().browse(uri);
                    // 关闭dialog
                    dialog.dispose();
                } catch (IOException | URISyntaxException ex) {
                    log.error(StrUtil.format("Failed to open link: {}", e.getURL()), ex);
                }
            }
        });

        // 设置对话框的大小和位置
        // 设置对话框的大小
        dialog.setSize(width, height);
        // 将对话框居中显示
        dialog.setLocationRelativeTo(null);
        // 显示对话框
        dialog.setVisible(true);
        return dialog;
    }

    /**
     * 创建主面板
     * @return JPanel
     */
    private void createFileChooser(JPanel mainPanel) {
        String workbenchLocation = ConfigUtils.getWorkbenchLocation();

        // 创建文本输入框
        JTextField textField = new JTextField(20);
        textField.setEnabled(false);
        textField.setDisabledTextColor(Color.BLACK);
        if (StrUtil.isNotEmpty(workbenchLocation)) {
            textField.setText(FileUtil.getName(workbenchLocation));
        }

        // 创建按钮
        JButton button = new JButton("选择");
        button.setFocusPainted(false);

        // 点击按钮打开文件选择
        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // 指定可选择的文件名
            fileChooser.setDialogTitle("选择直播工作台");
            // 指定exe后缀
            fileChooser.setFileFilter(liveWorkbenchFilter);
            // 指定可以选取的文件名
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                String name = FileUtil.getName(path);
                ConfigUtils.setWorkbenchLocation(path);
                textField.setText(name);
            }
        });

        // 创建按钮并按行手动添加
        JPanel rowPanel = new JPanel();
        // 元素左对齐
        rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 设置缩进
        rowPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        // 将组件添加到面板
        rowPanel.add(new JLabel("选择直播工作台:"));
        rowPanel.add(textField);
        rowPanel.add(button);
        mainPanel.add(rowPanel);
    }

    /**
     * 创建debug模式选择框
     */
    private void createDebugFrame(JPanel mainPanel) {
        // 创建公域直播选择框
        JCheckBox publicLive = new JCheckBox("公域直播");
        publicLive.setSelected(ConfigUtils.getPublicLive());
        publicLive.addActionListener(e -> {
            // 判断勾选状态
            log.info("公域直播, 勾选状态: {}", publicLive.isSelected());
            ConfigUtils.setPublicLive(publicLive.isSelected());
        });
        JPanel rowPanel = new JPanel();
        // 元素左对齐
        rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 设置缩进
        rowPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        rowPanel.add(publicLive);
        mainPanel.add(rowPanel);
    }

    /**
     * 创建主功能按钮
     */
    private void createMainFunctionButton(JPanel mainPanel) {
        // 调试按钮
        JButton debug = null;
        if (log.isDebugEnabled()) {
            debug = new JButton("调试");
            debug.setFocusPainted(false);
            debug.addActionListener(debugEventListener);
        }

        // 解绑按钮
        JButton unbind = new JButton("解绑");
        unbind.setFocusPainted(false);
        unbind.addActionListener(unbindEventListener);

        // 启动/停止按钮
        JButton startEnd = new JButton(VariableContainer.isRunning() ? "停止" : "启动");
        startEnd.setFocusPainted(false);
        startEnd.addActionListener(startStopEventListener);
        VariableContainer.setStateButton(startEnd);
        // 创建按钮并按行手动添加
        JPanel rowPanel = new JPanel();
        if (Objects.nonNull(debug)) {
            rowPanel.add(debug);
        }
        rowPanel.add(unbind);
        rowPanel.add(startEnd);
        mainPanel.add(rowPanel);
    }

    /**
     * 显示登录对话框 (会自动隐藏主界面)
     */
    public synchronized void showLoginDialog() {
        loginDialog.setVisible(true);
        loginDialog.resetMsg();
        frame.setVisible(false);
    }

    public synchronized void setState(int state) {
        if (Objects.nonNull(frame)) {
            frame.setState(state);
        }
    }

    /**
     * 设置窗口显示状态
     */
    public synchronized void setVisible(boolean visible) {
        if (Objects.nonNull(frame)) {
            frame.setVisible(visible);
        }
    }

    /**
     * 销毁窗口
     */
    public synchronized void close() {
        if (Objects.nonNull(frame)) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

}
