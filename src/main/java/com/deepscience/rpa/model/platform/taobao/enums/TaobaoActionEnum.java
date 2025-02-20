package com.deepscience.rpa.model.platform.taobao.enums;

import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.model.platform.taobao.constants.TaobaoActionConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author yangzhuo
 * @Description 淘宝动作枚举
 * @date 2025/2/12 16:43
 */
@Getter
@RequiredArgsConstructor
public enum TaobaoActionEnum implements ActionEnum {
    /**
     * 启动工作台
     */
    START_WORKBENCH(0, TaobaoActionConstants.START_WORKBENCH, "启动工作台"),
    /**
     * 查找工作台
     */
    FIND_WORKBENCH(1, TaobaoActionConstants.FIND_WORKBENCH, "查找工作台"),
    /**
     * 创建场次
     */
    CREATE_SESSION(2, TaobaoActionConstants.CREATE_SESSION, "创建场次"),
    /**
     * 开始直播
     */
    START_LIVING(3, TaobaoActionConstants.START_LIVING, "开始直播"),
    /**
     * 暂停直播
     */
    PAUSE_LIVING(4, TaobaoActionConstants.PAUSE_LIVING, "暂停直播"),
    /**
     * 结束直播
     */
    END_LIVING(5, TaobaoActionConstants.END_LIVING, "结束直播"),
    /**
     * 关闭工作台
     */
    CLOSE_WORKBENCH(6, TaobaoActionConstants.CLOSE_WORKBENCH, "关闭工作台"),
    /**
     * 获取直播id
     */
    DO_GET_LIVE_ID(7, TaobaoActionConstants.DO_GET_LIVE_ID, "获取直播id"),
    /**
     * 通过直播间id检索直播
     */
    SEARCH_LIVE_BY_ID(8, TaobaoActionConstants.SEARCH_LIVE_BY_ID, "通过直播间id检索直播"),

    /**
     * 处理验证码
     */
    HANDLE_VERIFICATION_CODE(100, TaobaoActionConstants.HANDLE_VERIFICATION_CODE, "处理验证码"),
    /**
     * 关闭所有窗口
     */
    CLOSE_ALL_WINDOW(101, TaobaoActionConstants.CLOSE_ALL_WINDOW, "关闭所有窗口"),
    /**
     * 关闭并退出
     */
    CLOSE_AND_EXIT(102, TaobaoActionConstants.CLOSE_AND_EXIT, "关闭并退出");

    /**
     * 动作编码
     */
    private final int code;

    /**
     * 动作名称
     */
    private final String action;

    /**
     * 动作描述
     */
    private final String desc;
}
