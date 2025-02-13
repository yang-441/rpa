package com.deepscience.rpa.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 程序参数配置
 * @author yangzhuo
 * @date 2025/1/26 15:23
 */
@Data
@Component
@ConfigurationProperties("app.frame.main")
public class MainFrameProperties {
    /**
     * 窗口宽度
     */
    private int width = 400;

    /**
     * 窗口高度
     */
    private int height = 200;

    /**
     * debug窗口高度
     */
    private int debugHeight = 160;

    /**
     * 消息宽度
     */
    private int msgWidth = 260;

    /**
     * 消息高度
     */
    private int msgHeight = 20;
}
