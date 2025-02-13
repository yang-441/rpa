package com.deepscience.rpa.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 运行状态枚举
 * @author yangzhuo
 * @date 2025/2/11 14:01
 */
@Getter
@RequiredArgsConstructor
public enum RunningStateEnum {
    /**
     * 停止
     */
    STOP(0, "停止", 1),
    /**
     * 运行中
     */
    RUNNING(1, "启动", 0),
    ;

    /**
     * 运行状态
     */
    private final int code;

    /**
     * 运行状态描述
     */
    private final String desc;

    /**
     * 运行状态转换
     */
    private final int changeCode;

    /**
     * 根据code获取枚举
     * @param code code
     * @return RunningStateEnum 运行状态枚举
     */
    public static RunningStateEnum getByCode(int code) {
        for (RunningStateEnum value : RunningStateEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return RunningStateEnum.STOP;
    }

    /**
     * 状态转换
     * @return RunningStateEnum 运行状态枚举
     */
    public RunningStateEnum change() {
        return getByCode(changeCode);
    }
}
