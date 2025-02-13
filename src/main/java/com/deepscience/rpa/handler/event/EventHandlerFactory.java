package com.deepscience.rpa.handler.event;

import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yangzhuo
 * @Description 事件处理器工厂
 * @date 2025/2/12 16:02
 */
@Slf4j
@Component
public class EventHandlerFactory {

    /**
     * 事件处理器table
     */
    private final Table<LivePlatform, ActionEventEnum, EventHandler> handlerTable = HashBasedTable.create();

    private EventHandlerFactory(List<EventHandler> eventHandlers) {
        for (EventHandler eventHandler : eventHandlers) {
            handlerTable.put(eventHandler.getLivePlatform(), eventHandler.getActionEvent(), eventHandler);
        }
    }

    /**
     * 处理事件
     * @param livePlan 直播计划
     */
    public void handler(LivePlanDTO livePlan) {
        EventHandler handler = getEventHandler(livePlan);
        if (Objects.nonNull(handler)) {
            handler.handle();
        } else {
            log.error("未找到事件处理器, livePlan: {}", livePlan);
        }
    }

    /**
     * 获取事件处理器
     * @param livePlan 直播计划
     * @return EventHandler 事件处理器
     */
    public EventHandler getEventHandler(LivePlanDTO livePlan) {
        return Optional.ofNullable(livePlan)
                .map(plan -> handlerTable.get(plan.getPlayPlatform(), plan.getActionEvent()))
                .orElse(null);
    }

    /**
     * 获取事件处理器
     * @param livePlatform  直播平台
     * @param actionEvent   执行事件
     * @return EventHandler 事件处理器
     */
    public EventHandler getEventHandler(LivePlatform livePlatform, ActionEventEnum actionEvent) {
        return handlerTable.get(livePlatform, actionEvent);
    }
}
