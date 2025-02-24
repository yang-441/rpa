package com.deepscience.rpa.model.platform.taobao.service.event.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.handler.action.ActionHandlerFactory;
import com.deepscience.rpa.model.event.service.ActionEventReportService;
import com.deepscience.rpa.model.platform.taobao.constants.TaobaoActionConstants;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoActionEnum;
import com.deepscience.rpa.model.platform.taobao.service.event.TaobaoEventHandler;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yangzhuo
 * @Description 淘宝开播事件处理器
 * @date 2025/2/12 16:39
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaobaoStartLiveEventHandlerImpl implements TaobaoEventHandler {

    /**
     * 卡点最大重试次数
     */
    @Value("${taobao.event.startLive.maxRetryTime:#{3}}")
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
        return ActionEventEnum.START_LIVING;
    }

    @Override
    public List<ActionEnum> getActions() {
        String liveId = VariableContainer.getActionContext().getLiveId();
        if (Objects.isNull(liveId)) {
            return TaobaoActionConstants.START_LIVING_ACTION_LIST;
        } else {
            return TaobaoActionConstants.START_LIVING_SEARCH_ACTION_LIST;
        }
    }

    @Override
    public int getMaxRetryTime() {
        return maxRetryTime;
    }

    @Override
    public void handle() {
        log.info("执行自动开播任务...");
        // 执行动作
        execute(actionHandlerFactory, this::handleException);
    }

    @Override
    public void errorReport() {
        ActionContext actionContext = VariableContainer.getActionContext();
        LivePlanDTO livePlan = actionContext.getLivePlan();
        actionEventReportService.errorReportAsync(livePlan);
        actionContext.setReportErrorPic(true);
    }

    private void handleException(Throwable t) {
        log.error("执行自动开播任务异常", t);
        actionHandlerFactory.handler(getLivePlatform(), TaobaoActionEnum.CLOSE_AND_EXIT);
        MsgUtils.writeErrorMsg(StrUtil.format("执行自动开播任务异常, 异常信息: {}", ExceptionUtil.getMessage(t)));
    }
}
