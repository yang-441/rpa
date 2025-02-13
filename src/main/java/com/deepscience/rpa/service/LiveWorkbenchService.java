package com.deepscience.rpa.service;

import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.deepscience.rpa.util.frame.FrameUtils;

/**
 * 直播工作台处理器
 * @author yangzhuo
 * @date 2025/1/24 14:19
 */
public interface LiveWorkbenchService {
    /**
     * 获取直播平台
     * @return LivePlatform
     */
    LivePlatform getLivePlatform();

    /**
     * 启动工作台
     * @return ActionContext
     */
    ActionContext startWorkbench();

    /**
     * 寻找直播工作台
     * @return ActionContext
     */
    ActionContext findWorkbench();

    /**
     * 点击创建场次
     * @return ActionContext
     */
    ActionContext createSession();

    /**
     * 处理验证码
     * @return ActionContext
     */
    ActionContext handleVerificationCode();

    /**
     * 开始直播
     * @return ActionContext
     */
    ActionContext startLiving();

    /**
     * 暂停直播
     * @return ActionContext
     */
    ActionContext pauseLiving();

    /**
     * 结束直播
     * @return ActionContext
     */
    ActionContext endLiving();

    /**
     * 获取直播间id
     * @return ActionContext
     */
    ActionContext doGetLiveId();

    /**
     * 关闭所有窗口
     * @return ActionContext 动作上下文
     */
    ActionContext closeAllWindows();

    /**
     * 关闭直播工作台
     * @return ActionContext
     */
    ActionContext closeWorkbench();

    /**
     * 关闭并退出程序
     * @return ActionContext
     */
    ActionContext closeAndExit();

    /**
     * 最小化应用程序
     */
    default void minimizeProgram() {
        FrameUtils.minimizeProgram();
    }

    /**
     * 显示登录对话框
     */
    default void showProgram() {
        FrameUtils.showProgram();
    }
}
