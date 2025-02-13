package com.deepscience.rpa;

import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.service.LoggingService;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.view.MainFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;

/**
 * 项目的启动类
 * HeadlessException 启动失败, 配置启动参数 -Djava.awt.headless=false
 * @author yangzhuo
 * @date 2025/1/26 15:26
 */
@Slf4j
@EnableScheduling
@EnableFeignClients(basePackages = "com.deepscience.rpa.rpc.api")
@EnableAspectJAutoProxy
@SpringBootApplication
public class AutoLiveApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AutoLiveApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String logLevel = null;
        // 解析启动参数
        for (String arg : args) {
            if (arg.startsWith("--logLevel=")) {
                logLevel = arg.split("=")[1].toLowerCase();
                break;
            }
        }
        // 根据启动参数自动配置日志级别
        LoggingService loggingService = SpringUtil.getBean(LoggingService.class);
        for (Map.Entry<String, String> entry : loggingService.getAllLogLevelConfig().entrySet()) {
            loggingService.setLogLevel(entry.getKey(), Objects.isNull(logLevel) ? entry.getValue() : logLevel);
        }
        // 校验绑定状态
        try {
            LoginBindService loginBindService = SpringUtil.getBean(LoginBindService.class);
            loginBindService.isValidBind();
        } catch (Exception e) {
            log.error("程序启动时, 校验绑定失败", e);
        }

        // 在 Swing 事件分发线程中启动 GUI
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = SpringUtil.getBean(MainFrame.class);
                mainFrame.run();
            } catch (Exception e) {
                log.error("mainFrame run error, 配置启动参数 -Djava.awt.headless=false", e);
                throw e;
            }
        });
    }
}
