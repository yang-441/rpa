package com.deepscience.rpa.rpc.api.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author luzhichao
 * @date 2022/4/25
 */
@Getter
@AllArgsConstructor
public enum PlayStatus {

    /**
     * 未开始
     */
    NOT_START("未开始"),
    /**
     * 开始中
     */
    STARTING("开始中"),
    /**
     * 推流中
     */
    STARTED("推流中"),
    /**
     * 开始失败
     */
    START_FAILED("开始失败"),
    /**
     * 结束中
     */
    ENDING("结束中"),
    /**
     * 已结束
     */
    ENDED("已结束"),
    /**
     * 结束失败
     */
    END_FAILED("结束失败"),
    /**
     * 推流失败
     */
    PUT_STREAM_FAILED("推流失败");

    private final String desc;
}
