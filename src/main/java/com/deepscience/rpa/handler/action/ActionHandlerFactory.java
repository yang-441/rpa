package com.deepscience.rpa.handler.action;

import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author yangzhuo
 * @Description 动作执行器工厂
 * @date 2025/2/12 15:58
 */
@Slf4j
@Component
public class ActionHandlerFactory {
    /**
     * 动作处理器table
     */
    private final Table<LivePlatform, ActionEnum, ActionHandler> handlerTable = HashBasedTable.create();

    private ActionHandlerFactory(List<ActionHandler> actionHandlers) {
        for (ActionHandler actionHandler : actionHandlers) {
            handlerTable.put(actionHandler.getLivePlatform(), actionHandler.getAction(), actionHandler);
        }
    }

    public void handler(LivePlatform livePlatform, ActionEnum actionEnum) {
        ActionHandler actionHandler = getActionHandler(livePlatform, actionEnum);
        if (Objects.nonNull(actionHandler)) {
            actionHandler.handle();
        } else {
            log.error("未找到动作处理器, livePlatform: {}, actionEnum: {}", livePlatform, actionEnum);
        }
    }

    /**
     * 获取动作处理器
     * @param livePlatform  直播平台
     * @param actionEnum    执行动作
     * @return ActionHandler
     */
    public ActionHandler getActionHandler(LivePlatform livePlatform, ActionEnum actionEnum) {
        return handlerTable.get(livePlatform, actionEnum);
    }
}
