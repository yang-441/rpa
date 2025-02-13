package com.deepscience.rpa.util;

import com.deepscience.rpa.common.container.VariableContainer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 窗口工具类
 * @author yangzhuo
 * @date 2025/1/24 17:15
 */
public class MsgUtils {

    /**
     * 一般消息颜色
     */
    public static final Color GENERAL_COLOR = new Color(0, 0, 0);

    /**
     * 成功颜色
     */
    public static final Color SUCCESS_COLOR = new Color(34, 139, 34);

    /**
     * 错误颜色
     */
    public static final Color ERROR_COLOR = new Color(255, 0, 0);

    /**
     * 警告颜色
     */
    public static final Color WARNING_COLOR = new Color(255, 140, 0);

    /**
     * 写入消息
     * @param msg 消息
     */
    public static void writeSuccessMsg(String msg) {
        JLabel messageLabel = VariableContainer.getMessageLabel();
        writeMsg(messageLabel, msg, SUCCESS_COLOR);
    }

    /**
     * 写入消息
     * @param msg 消息
     */
    public static void writeSuccessMsg(JLabel messageLabel, String msg) {
        writeMsg(messageLabel, msg, SUCCESS_COLOR);
    }

    /**
     * 写入错误消息
     * @param msg 消息
     */
    public static void writeErrorMsg(String msg) {
        JLabel messageLabel = VariableContainer.getMessageLabel();
        writeMsg(messageLabel, msg, ERROR_COLOR);
    }

    /**
     * 写入错误消息
     * @param msg 消息
     */
    public static void writeErrorMsg(JLabel messageLabel, String msg) {
        writeMsg(messageLabel, msg, ERROR_COLOR);
    }

    /**
     * 写入警告消息
     * @param msg 消息
     */
    public static void writeWarningMsg(String msg) {
        JLabel messageLabel = VariableContainer.getMessageLabel();
        writeMsg(messageLabel, msg, WARNING_COLOR);
    }

    /**
     * 写入警告消息
     * @param msg 消息
     */
    public static void writeWarningMsg(JLabel messageLabel, String msg) {
        writeMsg(messageLabel, msg, WARNING_COLOR);
    }

    /**
     * 写入一般消息
     * @param msg 消息
     */
    public static void writeInfoMsg(String msg) {
        JLabel messageLabel = VariableContainer.getMessageLabel();
        writeMsg(messageLabel, msg, GENERAL_COLOR);
    }

    /**
     * 写入一般消息
     * @param messageLabel 消息文本框
     * @param msg          消息内容
     */
    public static void writeInfoMsg(JLabel messageLabel, String msg) {
        writeMsg(messageLabel, msg, GENERAL_COLOR);
    }

    private synchronized static void writeMsg(JLabel messageLabel, String msg, Color color) {
        if (Objects.isNull(messageLabel)) {
            return;
        }
        messageLabel.setForeground(color);
        messageLabel.setText(Objects.isNull(msg) ? "" : msg);
    }
}
