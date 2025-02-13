package com.deepscience.rpa.handler.action;

import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;

/**
 * 动作执行器
 * @author yangzhuo
 * @date 2025/1/26 16:32
 */
public interface ActionHandler {
    /**
     * 获取直播平台
     * @return LivePlatform
     */
    LivePlatform getLivePlatform();

    /**
     * 获取执行动作
     * @return ActionEnum
     */
    ActionEnum getAction();

    /**
     * 动作执行
     */
    void handle();
}
