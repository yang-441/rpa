package com.deepscience.rpa.model.platform.taobao.service.event.impl;

import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.handler.action.ActionHandlerFactory;
import com.deepscience.rpa.model.event.service.ActionEventReportService;
import com.deepscience.rpa.model.platform.taobao.constants.TaobaoActionConstants;
import com.deepscience.rpa.model.platform.taobao.service.event.TaobaoEventHandler;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yangzhuo
 * @Description 淘宝关播事件处理器
 * @date 2025/2/12 16:40
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaobaoEndLiveEventHandlerImpl implements TaobaoEventHandler {

    /**
     * 卡点最大重试次数
     */
    @Value("${taobao.event.endLive.maxRetryTime:#{3}}")
    private int maxRetryTime;

    /**
     * 动作处理器工厂
     */
    private final ActionHandlerFactory actionHandlerFactory;

    /**
     * 执行动作事件上报服务
     */
    private final ActionEventReportService actionEventReportService;

    @Override
    public ActionEventEnum getActionEvent() {
        return ActionEventEnum.END_LIVING;
    }

    @Override
    public List<ActionEnum> getActions() {
        return TaobaoActionConstants.END_LIVING_ACTION_LIST;
    }

    @Override
    public int getMaxRetryTime() {
        return maxRetryTime;
    }

    @Override
    public void handle() {
        log.info("执行自动关播任务...");
        // 执行动作
        execute(actionHandlerFactory);
    }

    @Override
    public void handleException() {
        LivePlanDTO livePlan = VariableContainer.getActionContext().getLivePlan();
        actionEventReportService.errorReportAsync(livePlan);
    }
}
