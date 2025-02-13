package com.deepscience.rpa.rpc.api.event.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 动作事件枚举
 * @author yangzhuo
 * @date 2025/2/11 15:13
 */
@Getter
@RequiredArgsConstructor
public enum ActionEventEnum {
    /**
     * 结束直播
     */
    END_LIVING(0, "结束直播"),
    /**
     * 开始直播
     */
    START_LIVING(1, "开始直播"),

    ;
    /**
     * 动作事件编码
     */
    private final int code;
    /**
     * 动作事件描述
     */
    private final String desc;
}
