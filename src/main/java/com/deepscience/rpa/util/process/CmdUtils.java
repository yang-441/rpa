package com.deepscience.rpa.util.process;

import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.util.process.killer.WindowsCmdUtils;

/**
 * @author yangzhuo
 * @Description 杀死进程工具类
 * @date 2025/2/18 14:35
 */
public class CmdUtils {
    /**
     * 当前程序运行的操作系统
     */
    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * 通过进程名称杀死进程
     * @param processName 进程名称
     * @return boolean
     */
    public static boolean kill(String processName) {
        // windows
        if (OS.contains("win")) {
            return WindowsCmdUtils.kill(processName);
        } else {
            // 抛出不支持异常
            throw new UnsupportedOperationException(StrUtil.format("不支持的操作系统: {}", OS));
        }
    }

    public static boolean minimizeWindow() {
        // windows
        if (OS.contains("win")) {
            return WindowsCmdUtils.minimizeWindow();
        } else {
            // 抛出不支持异常
            throw new UnsupportedOperationException(StrUtil.format("不支持的操作系统: {}", OS));
        }
    }
}
