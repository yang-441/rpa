package com.deepscience.rpa.util.frame;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.util.process.CmdUtils;
import com.deepscience.rpa.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.Optional;

/**
 * 窗口工具类
 * @author yangzhuo
 * @date 2025/2/8 15:06
 */
public class FrameUtils {

    /**
     * 最小化面板
     */
    public static void minimizeProgram() {
        CmdUtils.minimizeWindow();
    }

    /**
     * 弹出面板
     */
    public static void showProgram() {
        Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                .ifPresent(mainFrame -> {
                    mainFrame.setState(JFrame.NORMAL);
                });
    }

    /**
     * 创建消息面板
     * @param mainPanel  主面板
     * @param defaultMsg 默认消息
     * @param msgWidth   默认消息宽度
     * @param msgHeight  默认消息高度
     */
    public static JLabel createMsgPanel(JPanel mainPanel, String defaultMsg, int msgWidth, int msgHeight) {
        JPanel msgPanel = new JPanel();
        // 设置左对齐
        msgPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // 设置消息内容
        JLabel titleLabel = new JLabel("提示信息: ");

        // 创建消息标签
        String msg = StrUtil.isEmpty(defaultMsg) ? StrUtil.EMPTY : defaultMsg;
        JLabel msgLabel = new JLabel(msg);

        JScrollPane scrollPane = createMsgLabel(msgLabel, msgWidth, msgHeight);

        msgPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        msgPanel.add(titleLabel);
        msgPanel.add(scrollPane);
        mainPanel.add(msgPanel);
        return msgLabel;
    }

    /**
     * 创建消息滚动面板
     * @param msgLabel 消息标签
     * @return JScrollPane 滚动面板
     */
    private static JScrollPane createMsgLabel(JLabel msgLabel, int msgWidth, int msgHeight) {
        // 将 msgLabel 包裹在 JScrollPane 中（为了启用自动滚动效果）
        JScrollPane scrollPane = getScrollPane(msgLabel, msgWidth, msgHeight);

        // 创建定时器，每 10 毫秒移动一次文本
        Timer timer = new Timer(10, new ActionListener() {
            int x = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                // 判断文本长度是否超限
                if (msgLabel.getPreferredSize().width < scrollPane.getWidth()) {
                    return;
                }
                // 移动文本，水平滚动
                x -= 1; // 每次将文本向左移动 1 像素
                if (x < -msgLabel.getPreferredSize().width) {
                    x = scrollPane.getWidth(); // 如果文本完全滚出屏幕，重新开始
                }
                msgLabel.setLocation(x, 0);
            }
        });
        timer.start();
        // 组件销毁时自动关闭timer
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {
                System.out.println(111111111);
                timer.stop();
            }
        });
        return scrollPane;
    }

    /**
     * 获取滚动面板
     * @param msgLabel 文本标签
     * @return JScrollPane
     */
    private static JScrollPane getScrollPane(JLabel msgLabel, int msgWidth, int msgHeight) {
        JScrollPane scrollPane = new JScrollPane(msgLabel);
        // 推荐尺寸
        scrollPane.setPreferredSize(new Dimension(msgWidth, msgHeight));
        // 最小尺寸
        scrollPane.setMinimumSize(new Dimension(msgWidth, msgHeight));
        // 最大尺寸
        scrollPane.setMaximumSize(new Dimension(msgWidth, msgHeight));

        // 使用 setBounds 来设置 JScrollPane 的位置和固定大小
        scrollPane.setBounds(0, 0, msgWidth, msgHeight);
        // 隐藏边框
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}
