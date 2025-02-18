package com.deepscience.rpa.util.process.killer;

import cn.hutool.core.util.CharsetUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.util.concurrent.TimeUnit;

/**
 * @author yangzhuo
 * @Description windows杀进程工具
 * @date 2025/2/18 14:36
 */
@Slf4j
public class WindowsProcessKillerUtils {

    /**
     * 通过进程名称杀死进程
     * @param processName 进程名称
     * @return boolean
     */
    public static boolean kill(String processName) {
        try {
            // 获取任务管理器中的进程列表
            ProcessBuilder builder = new ProcessBuilder("tasklist", "/FI", "IMAGENAME eq " + processName);
            Process process = builder.start();

            // 获取命令的输出
            @Cleanup BufferedReader reader = process.inputReader(CharsetUtil.CHARSET_GBK);
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains(processName)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                // 进程存在，终止它
                log.info("正在终止进程: {}", processName);
                Process killProcess = new ProcessBuilder("taskkill", "/F", "/IM", processName).start();
                return killProcess.waitFor(10, TimeUnit.SECONDS);
            } else {
                log.info("进程不存在: {}", processName);
                return false;
            }
        } catch (Exception e) {
            log.error("kill process error", e);
        }
        return false;
    }
}
