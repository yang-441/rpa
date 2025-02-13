package com.deepscience.rpa.common.config.feign;

import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.config.AppConfig;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 配置
 * @author yangzhuo
 * @date 2025/2/10 21:11
 */
@Configuration
@RequiredArgsConstructor
public class FeignConfig {
    /**
     * app配置
     */
    private final AppConfig appConfig;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String clientCredentials = appConfig.getClientCredentials();
            // 添加 Authorization 头
            if (StrUtil.isNotEmpty(clientCredentials)) {
                template.header("Authorization", clientCredentials);
            }
        };
    }
}