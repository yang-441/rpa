package com.deepscience.rpa.util.frame.entity;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

/**
 * @author yangzhuo
 * @Description 启动画面类
 * @date 2025/2/17 12:04
 */
@Setter
@Getter
public class SplashScreen {

    private JWindow splashWindow;

    private String imageResource;

    public SplashScreen() {
        splashWindow = new JWindow();
        splashWindow.setLayout(new BorderLayout());
        splashWindow.setBackground(new Color(0, 0, 0, 0));

        // 创建显示图标的 JLabel
        JLabel iconLabel = new JLabel();
        String finalImage;
        if (StrUtil.isBlank(imageResource)) {
            finalImage = "images/company.gif";
        } else {
            finalImage = imageResource;
        }
        ImageIcon icon = new ImageIcon(ResourceUtil.getResource(finalImage));
        iconLabel.setIcon(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        splashWindow.getContentPane().add(iconLabel, BorderLayout.NORTH);

        // 显示的文字
        JLabel label = new JLabel("客户端启动中...", JLabel.CENTER);
        Font defaultFont = label.getFont();
        label.setFont(defaultFont.deriveFont(Font.BOLD, 20));
        splashWindow.getContentPane().add(label, BorderLayout.CENTER);

        splashWindow.setSize(400, 120);
        // 居中显示
        splashWindow.setLocationRelativeTo(null);
        // 禁止移动窗口
        splashWindow.setAlwaysOnTop(true);
        splashWindow.setVisible(true);
    }

    public void hideSplash() {
        splashWindow.setVisible(false);
    }
}