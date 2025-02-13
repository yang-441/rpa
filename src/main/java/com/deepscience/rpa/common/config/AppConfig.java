package com.deepscience.rpa.common.config;

import cn.hutool.core.io.FileUtil;
import com.deepscience.rpa.common.constants.AppFileConstants;
import com.deepscience.rpa.common.container.VariableContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 项目配置, 从用户磁盘读取配置, 配置变更自动持久化
 * @author yangzhuo
 * @date 2025/1/23 17:55
 */
@Slf4j
@Component
public class AppConfig {
    /**
     * 直播工作台路径
     */
    private static final String WORKBENCH_LOCATION = "workbenchLocation";

    /**
     * 上次屏幕位置
     */
    private static final String LAST_SCREEN_ID = "lastScreenId";

    /**
     * 公域直播
     */
    private static final String PUBLIC_LIVE = "publicLive";

    /**
     * 数字人直播
     */
    private static final String DIGITAL_LIVE = "digitalLive";

    /**
     * 用户名
     */
    private static final String USERNAME = "username";

    /**
     * 客户端凭证
     */
    private static final String CLIENT_CREDENTIALS = "clientCredentials";

    /**
     * 线程安全的配置文件
     */
    private PropertiesConfiguration config;

    @Bean
    public PropertiesConfiguration propertiesConfiguration() throws ConfigurationException {
        FileBasedConfigurationBuilder<PropertiesConfiguration> builder = fileBasedConfigurationBuilder();
        builder.setAutoSave(true);
        config = builder.getConfiguration();
        return config;
    }

    private FileBasedConfigurationBuilder<PropertiesConfiguration> fileBasedConfigurationBuilder() {
        File configFile = FileUtil.touch(AppFileConstants.CONFIG_PATH);
        return new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().fileBased().setFile(configFile));
    }

    /**
     * 获取应用地址
     * @return String
     */
    public String getWorkbenchLocation() {
        try {
            return config.getString(WORKBENCH_LOCATION);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置应用地址
     * @param workbenchLocation 应用地址
     */
    public void setWorkbenchLocation(String workbenchLocation) {
        config.setProperty(WORKBENCH_LOCATION, workbenchLocation);
    }

    public Integer getLastScreenId() {
        try {
            return config.getInteger(LAST_SCREEN_ID, 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setLastScreenId(Integer lastScreenLocation) {
        config.setProperty(LAST_SCREEN_ID, lastScreenLocation);
    }

    /**
     * 获取公域直播配置
     * @return boolean
     */
    public boolean getPublicLive() {
        try {
            return config.getBoolean(PUBLIC_LIVE, false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置公域直播配置
     * @param publicLive 公域直播配置
     */
    public void setPublicLive(boolean publicLive) {
        config.setProperty(PUBLIC_LIVE, publicLive);
    }

    /**
     * 获取数字人直播配置
     * @return boolean
     */
    public boolean getDigitalLive() {
        try {
            return config.getBoolean(DIGITAL_LIVE, true);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置数字人直播配置
     * @param digitalLive 数字人直播配置
     */
    public void setDigitalLive(boolean digitalLive) {
        config.setProperty(DIGITAL_LIVE, digitalLive);
    }

    /**
     * 获取用户名
     * @return String 用户名
     */
    public String getUsername() {
        try {
            return config.getString(USERNAME);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置用户名
     * @param username 用户名
     */
    public void setUsername(String username) {
        config.setProperty(USERNAME, username);
    }

    /**
     * 获取客户端凭证
     * @return String
     */
    public String getClientCredentials() {
        try {
            return config.getString(CLIENT_CREDENTIALS);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置客户端凭证
     * @param clientCredentials 客户端凭证
     */
    public void setClientCredentials(String clientCredentials) {
        config.setProperty(CLIENT_CREDENTIALS, clientCredentials);
        VariableContainer.putCaffeineCache(clientCredentials, null);
    }
}
