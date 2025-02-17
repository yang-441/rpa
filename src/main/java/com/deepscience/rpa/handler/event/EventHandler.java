package com.deepscience.rpa.handler.event;

import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;

import java.util.List;

/**
 * @author yangzhuo
 * @Description 事件处理器
 * @date 2025/2/12 16:02
 */
public interface EventHandler {
    /**
     * 获取直播平台
     * @return LivePlatform
     */
    LivePlatform getLivePlatform();

    /**
     * 获取动作事件
     * @return ActionEventEnum
     */
    ActionEventEnum getActionEvent();

    /**
     * 获取动作列表
     * @return List<ActionEnum> 动作列表
     */
    List<ActionEnum> getActions();

    /**
     * 获取最大重试次数
     * @return int 最大重试次数
     */
    int getMaxRetryTime();

    /**
     * 处理事件
     */
    void handle();

    /**
     * 处理异常情况
     */
    void handleException();
}
