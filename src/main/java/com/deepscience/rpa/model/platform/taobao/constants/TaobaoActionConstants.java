package com.deepscience.rpa.model.platform.taobao.constants;

import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoActionEnum;

import java.util.List;

/**
 * @author yangzhuo
 * @Description 淘宝动作枚举
 * @date 2025/2/12 16:51
 */
public class TaobaoActionConstants {

    /**
     * 开播动作列表
     */
    public static final List<ActionEnum> START_LIVING_ACTION_LIST = List.of(
            // 1-启动工作台
            TaobaoActionEnum.START_WORKBENCH,
            // 2-查找工作台
            TaobaoActionEnum.FIND_WORKBENCH,
            // 3-创建场次
            TaobaoActionEnum.CREATE_SESSION,
            // 4-开始直播
            TaobaoActionEnum.START_LIVING,
            // 5-暂停直播
            TaobaoActionEnum.PAUSE_LIVING,
            // 6-获取直播间id
            TaobaoActionEnum.DO_GET_LIVE_ID,
            // 7-关闭所有弹窗
            TaobaoActionEnum.CLOSE_ALL_WINDOW,
            // 8-关闭工作台
            TaobaoActionEnum.CLOSE_WORKBENCH
    );

    /**
     * 结束直播动作列表
     */
    public static final List<ActionEnum> END_LIVING_ACTION_LIST = List.of(
            // 1-启动工作台
            TaobaoActionEnum.START_WORKBENCH,
            // 2-查找工作台
            TaobaoActionEnum.FIND_WORKBENCH,
            // 3-获取直播间id
            TaobaoActionEnum.DO_GET_LIVE_ID,
            // 4-结束直播
            TaobaoActionEnum.END_LIVING,
            // 5-关闭所有弹窗
            TaobaoActionEnum.CLOSE_ALL_WINDOW,
            // 6-关闭工作台
            TaobaoActionEnum.CLOSE_WORKBENCH
    );

    /**
     * 启动工作台
     */
    public static final String START_WORKBENCH = "taobao_cation_startWorkbench";

    /**
     * 查找工作台
     */
    public static final String FIND_WORKBENCH = "taobao_cation_findWorkbench";

    /**
     * 创建场次
     */
    public static final String CREATE_SESSION = "taobao_cation_createSession";

    /**
     * 开始直播
     */
    public static final String START_LIVING = "taobao_cation_startLiving";

    /**
     * 暂停直播
     */
    public static final String PAUSE_LIVING = "taobao_cation_pauseLiving";

    /**
     * 结束直播
     */
    public static final String END_LIVING = "taobao_cation_endLiving";

    /**
     * 关闭工作台
     */
    public static final String CLOSE_WORKBENCH = "taobao_cation_closeWorkbench";

    /**
     * 处理验证码
     */
    public static final String HANDLE_VERIFICATION_CODE = "taobao_cation_handleVerificationCode";

    /**
     * 关闭所有窗口
     */
    public static final String CLOSE_ALL_WINDOW = "taobao_cation_closeAllWindow";

    /**
     * 执行获取直播间id
     */
    public static final String DO_GET_LIVE_ID = "taobao_cation_doGetLiveId";

    /**
     * 关闭并退出
     */
    public static final String CLOSE_AND_EXIT = "taobao_cation_closeAndExit";
}
