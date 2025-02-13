package com.deepscience.rpa.model.platform.taobao.service.event;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.handler.action.ActionHandlerFactory;
import com.deepscience.rpa.handler.event.EventHandler;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoActionEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.deepscience.rpa.util.MsgUtils;

/**
 * @author yangzhuo
 * @Description
 * @date 2025/2/12 16:37
 */
public interface TaobaoEventHandler extends EventHandler {
    /**
     * 获取直播平台
     * @return LivePlatform 直播平台
     */
    @Override
    default LivePlatform getLivePlatform() {
        return LivePlatform.TAOBAO;
    }

    /**
     * 初始化上下文
     * @return ActionContext 动作上下文
     */
    default ActionContext initContext() {
        ActionContext actionContext = VariableContainer.getActionContext();
        actionContext.setLivePlatform(getLivePlatform());
        actionContext.setActionEvent(getActionEvent());
        actionContext.setActions(getActions());
        actionContext.setCurrentAction(getActions().get(0));
        actionContext.setHasNext(true);
        return actionContext;
    }

    /**
     * 执行动作
     * @param actionHandlerFactory 动作处理器工厂
     */
    default void execute(ActionHandlerFactory actionHandlerFactory) {
        // 初始化上下文
        ActionContext actionContext = initContext();
        // 驱动执行
        ActionEnum currentAction;
        while ((currentAction = actionContext.getCurrentAction()) != null) {
            actionHandlerFactory.handler(getLivePlatform(), currentAction);
            // 迭代到下一个动作节点
            if (!actionContext.next()) {
                if (VariableContainer.isRunning()) {
                    // 异常处理
                    actionHandlerFactory.handler(getLivePlatform(), TaobaoActionEnum.CLOSE_AND_EXIT);
                } else {
                    RunningStateEnum state = VariableContainer.changeRunningState();
                    MsgUtils.writeInfoMsg(StrUtil.format("切换运行状态, 当前状态[{}]", state.getDesc()));
                }
                break;
            }
            ThreadUtil.safeSleep(100);
        }
    }
}
