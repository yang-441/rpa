package com.deepscience.rpa.task;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.exception.util.ServiceExceptionUtil;
import com.deepscience.rpa.handler.event.EventHandlerFactory;
import com.deepscience.rpa.model.event.service.ActionEventReportService;
import com.deepscience.rpa.model.live.service.LivePlanService;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.task.entity.DelayTaskModel;
import com.deepscience.rpa.task.entity.TaskModel;
import com.deepscience.rpa.util.MsgUtils;
import com.deepscience.rpa.util.frame.FrameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务调度工具
 * @author yangzhuo
 * @date 2025/1/24 17:36
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JobScheduling {
    /**
     * 任务队列
     */
    private final static LinkedBlockingDeque<TaskModel> TASK_QUEUE = new LinkedBlockingDeque<>();

    /**
     * 延时任务队列
     */
    private final static DelayQueue<DelayTaskModel> DELAY_QUEUE = new DelayQueue<>();

    /**
     * 事件处理器工厂
     */
    private final EventHandlerFactory eventHandlerFactory;

    /**
     * 登录绑定服务
     */
    private final LoginBindService loginBindService;

    /**
     * 直播计划服务
     */
    private final LivePlanService livePlanService;

    /**
     * 动作事件上报服务
     */
    private final ActionEventReportService actionEventReportService;

    /**
     * 定时任务全局锁
     */
    private final static ReentrantLock LOCK = new ReentrantLock();

    /**
     * 调度执行延迟任务
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 100)
    public void execDelayTask() {
        try {
            if (VariableContainer.isRunning() && loginBindService.isValidBind()) {
                doDelayTask();
            }
        } catch (Exception e) {
            log.error("execDelayTask error", e);
            MsgUtils.writeErrorMsg(StrUtil.format("执行失败, 异常信息: {}", ExceptionUtil.getMessage(e)));
        }
    }

    /**
     * 调度更新直播计划任务队列
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 60 * 1000)
    public void updateLivePlanTaskQueue() {
        try {
            if (VariableContainer.isRunning() && loginBindService.isValidBind()) {
                // 获取锁
                LOCK.lock();
                try {
                    livePlanService.updateLivePlanTaskQueue();
                    int size = DELAY_QUEUE.size();
                    MsgUtils.writeSuccessMsg(StrUtil.format("任务队列已更新, 任务数[{}], 等待执行...", size));
                } finally {
                    LOCK.unlock();
                }
            }
        } catch (Exception e) {
            log.error("updateLivePlanTaskQueue error", e);
            MsgUtils.writeErrorMsg(StrUtil.format("执行失败, 异常信息: {}", ExceptionUtil.getMessage(e)));
        }
    }

    /**
     * 调度执行任务
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 200)
    public void execTask() {
        if (VariableContainer.isRunning()) {
            doTask();
        }
    }

    /**
     * 重试上报
     */
    @Scheduled(initialDelay = 1000, fixedRate = 200)
    public void retryReport() {
        try {
            actionEventReportService.retryReport();
        } catch (Exception e) {
            log.error("retryReport error", e);
        }
    }

    /**
     * 添加任务
     * @param task 任务
     */
    public static <R> void offerTask(TaskModel<R> task) {
        try {
            TASK_QUEUE.put(task);
        } catch (Exception e) {
            log.error("offer task error", e);
        }
    }

    /**
     * 添加延迟任务
     * @param task 任务
     */
    public static <R> void putDelayTask(DelayTaskModel<R> task) {
        if (Objects.isNull(task)) {
            return;
        }
        try {
            if (!DELAY_QUEUE.contains(task)) {
                DELAY_QUEUE.put(task);
            }
        } catch (Exception e) {
            log.error("put delay task error", e);
        }
    }

    /**
     * 执行任务
     */
    private void doTask() {
        try {
            TaskModel taskModel = TASK_QUEUE.pollFirst();
            if (Objects.isNull(taskModel)) {
                return;
            }
            CompletableFuture callback = taskModel.getCallback();
            try {
                Object res = taskModel.getCallable().call();
                callback.complete(res);
            } catch (Exception e) {
                callback.completeExceptionally(e);
            }
        } catch (Exception e) {
            log.error("do task error", e);
        }
    }

    /**
     * 执行延迟任务
     */
    private void doDelayTask() {
        try {
            DelayTaskModel delayTaskModel = DELAY_QUEUE.poll();
            if (Objects.isNull(delayTaskModel)) {
                return;
            }
            doDelayTaskWithLock(delayTaskModel);
        } catch (Exception e) {
            log.error("do task error", e);
        }
    }

    private void doDelayTaskWithLock(DelayTaskModel delayTaskModel) {
        // 获取锁
        LOCK.lock();
        CompletableFuture callback = null;
        try {
            callback = delayTaskModel.getCallback();
            boolean result = false;
            log.info("执行延迟任务: {}", delayTaskModel);
            Object param = delayTaskModel.getParam();
            if (param instanceof LivePlanDTO) {
                LivePlanDTO livePlan = (LivePlanDTO) param;
                if (!livePlanService.haveLivePlan(livePlan)) {
                    log.info("直播计划不存在, 已经执行或已修改, livePlan: {}", livePlan);
                    return;
                }
                // 最小化应用
                FrameUtils.minimizeProgram();
                // 初始化上下文
                ActionContext actionContext = VariableContainer.getActionContext();
                actionContext.setLivePlan(livePlan);
                actionContext.setLiveId(livePlan.getLiveAccount());
                actionContext.setPushUrl(livePlan.getLiveUrl());
                // 开播时, 未填写直播间推流地址, 执行自动开播
                if (ActionEventEnum.START_LIVING.equals(livePlan.getActionEvent())
                        && StrUtil.isNotBlank(livePlan.getLiveUrl())) {
                    log.info("开播时, 已填写直播间推流地址, 不执行自动化任务, livePlan: {}", livePlan);
                } else {
                    // 执行事件处理
                    eventHandlerFactory.handler(livePlan);
                }
                // 上报信息
                result = actionEventReportService.reportInfo(livePlan);
                callback.complete(param);
            } else {
                log.error("delay task param error, param: {}", param);
                callback.completeExceptionally(ServiceExceptionUtil.invalidParamException("delay task param error"));
            }
            log.info("执行延迟任务结束, result: {}, info: {}", result, delayTaskModel);
        } catch (Exception e) {
            log.error(StrUtil.format("执行延迟任务完成异常: info: {}", delayTaskModel), e);
            if (callback != null) {
                callback.completeExceptionally(e);
            }
        } finally {
            VariableContainer.removeActionContext();
            // 最大化应用
            FrameUtils.showProgram();
            LOCK.unlock();
        }
    }
}
