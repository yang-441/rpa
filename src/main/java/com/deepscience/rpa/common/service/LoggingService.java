package com.deepscience.rpa.common.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 日志动态配置类
 * @author yangzhuo
 * @date 2025/2/6 09:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingService {


    private final ConfigurableEnvironment environment;

    /**
     * 动态调整日志级别
     * @param loggerName 日志记录器的名称
     * @param level 日志级别
     */
    public void setLogLevel(String loggerName, String level) {
        log.info("调整日志记录器 [{}] 的日志级别为 [{}]", loggerName, level);
        // 获取LoggerContext
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 获取指定名称的 Logger
        Logger logger = context.getLogger(loggerName);
        logger.setLevel(Level.valueOf(level));
    }

    /**
     * 全局修改所有日志记录器的日志级别
     * @param level 日志级别
     */
    public void setGlobalLogLevel(String level) {
        // 获取LoggerContext
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 遍历所有日志记录器并设置日志级别
        for (Logger logger : context.getLoggerList()) {
            logger.setLevel(Level.valueOf(level));
        }
    }

    /**
     * 获取所有以 logging.level. 开头的配置键
     * @return Map<String,String>
     */
    public Map<String, String> getAllLogLevelConfig() {
        return environment.getPropertySources().stream()
                .flatMap(propertySource -> {
                    // 获取 PropertySource 的源，确保它是 Map 类型
                    Object source = propertySource.getSource();
                    if (source instanceof Map) {
                        return ((Map<String, Object>) source).entrySet().stream();
                    } else {
                        return null;
                    }
                })
                .filter(entry -> entry.getKey().startsWith("logging.level."))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().replace("logging.level.", ""),
                        entry -> String.valueOf(entry.getValue())
                ));
    }
}
