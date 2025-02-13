package com.deepscience.rpa.rpc.api.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 直播平台枚举类
 * @author yangzhuo
 * @date 2025/1/24 14:49
 */
@Getter
@AllArgsConstructor
public enum LivePlatform {
    /**
     * 淘宝
     */
    TAOBAO(0, "淘宝"),
    /**
     * 拼多多
     */
    PINDUODUO(1, "拼多多"),
    /**
     * 抖音
     */
    DOUYIN(2, "抖音"),
    /**
     * 京东
     */
    JINGDONG(3, "京东"),
    /**
     * 微信视频号
     */
    WEIXIN(4, "微信视频号"),
    /**
     * 支付宝
     */
    ZHIFUBAO(5, "支付宝"),
    /**
     * 美团
     */
    MEITUAN(6, "美团"),
    /**
     * 快手
     */
    KUAISHOU(7, "快手");

    /**
     * id
     */
    private final int id;

    /**
     * 描述
     */
    private final String desc;
}
