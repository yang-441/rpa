package com.deepscience.rpa.util;

import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.config.AppConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 配置工具
 * @author yangzhuo
 * @date 2025/1/23 17:59
 */
@Slf4j
public class ConfigUtils {

    /**
     * 获取直播中控台执行地址
     * @return String
     */
    public static String getWorkbenchLocation() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getWorkbenchLocation).orElse(null);
    }

    /**
     * 设置直播中控台执行地址
     * @param workbenchLocation 直播中控台执行地址
     */
    public static void setWorkbenchLocation(String workbenchLocation) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setWorkbenchLocation(workbenchLocation);
    }

    /**
     * 获取上次使用的屏幕编号
     * @return Integer 屏幕编号
     */
    public static Integer getLastScreenId() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getLastScreenId).orElse(null);
    }

    /**
     * 设置上次使用的屏幕编号
     * @param lastScreenId 屏幕编号
     */
    public static void setLastScreenId(Integer lastScreenId) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setLastScreenId(lastScreenId);
    }

    /**
     * 获取是否为公开直播配置
     * @return boolean
     */
    public static boolean getPublicLive() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getPublicLive).orElse(false);
    }

    /**
     * 设置是否为公开直播配置
     * @param publicLive 是否为公开直播
     */
    public static void setPublicLive(boolean publicLive) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setPublicLive(publicLive);
    }

    /**
     * 获取是否为数字人直播配置
     * @return boolean
     */
    public static boolean getDigitalLive() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getDigitalLive).orElse(true);
    }

    /**
     * 设置是否为数字人直播配置
     * @param digitalLive 是否为数字人直播
     */
    public static void setDigitalLive(boolean digitalLive) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setDigitalLive(digitalLive);
    }

    /**
     * 获取客户端凭证
     * @return String
     */
    public static String getClientCredentials() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getClientCredentials).orElse(null);
    }

    /**
     * 设置客户端凭证
     * @param clientCredentials 客户端凭证
     */
    public static void setClientCredentials(String clientCredentials) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setClientCredentials(clientCredentials);
    }

    /**
     * 获取用户名
     * @return String
     */
    public static String getUsername() {
        return Optional.of(SpringUtil.getBean(AppConfig.class)).map(AppConfig::getUsername).orElse(null);
    }

    /**
     * 设置用户名
     * @param username 用户名
     */
    public static void setUsername(String username) {
        AppConfig appConfig = SpringUtil.getBean(AppConfig.class);
        appConfig.setUsername(username);
    }
}
