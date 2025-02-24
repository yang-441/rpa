package com.deepscience.rpa.common.container;

import cn.hutool.extra.spring.SpringUtil;
import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.service.LiveWorkbenchService;
import com.deepscience.rpa.view.MainFrame;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 变量容器
 * @author yangzhuo
 * @date 2025/1/24 15:11
 */
@Slf4j
public abstract class VariableContainer {
    /**
     * 运行状态
     */
    private static volatile RunningStateEnum RUNNING_STATE = RunningStateEnum.STOP;

    /**
     * 变量容器, 用于统一存储管理
     */
    private static final ConcurrentHashMap<String, Object> CONTAINER = new ConcurrentHashMap<>(255);

    /**
     * Caffeine 缓存
     */
    private static final Cache<String, Object> CACHE = Caffeine.newBuilder()
            .maximumSize(256)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    /**
     * 全局处理器
     */
    private static final String PROCESS = "liveWorkbenchProcess";

    /**
     * 全局上下文
     */
    private static final String APP_CONTEXT = "actionContext";

    /**
     * 消息标签
     */
    private static final String MESSAGE_LABEL = "messageLabel";

    /**
     * 状态按钮
     */
    private static final String STATE_BUTTON = "stateButton";

    /**
     * 开播成功次数
     */
    private static final String START_SUCCESS_COUNT = "startSuccessCount";

    /**
     * 开播失败次数
     */
    private static final String START_FAIL_COUNT = "startFailCount";

    /**
     * 设置全局运行状态
     * @param runningState 运行状态
     */
    public synchronized static void setRunningState(RunningStateEnum runningState) {
        RUNNING_STATE = runningState;
        getActionContext().setHasNext(RunningStateEnum.RUNNING.equals(runningState));
        Optional.ofNullable(getStateButton())
                .ifPresent(stateButton -> {
                    stateButton.setText(isRunning() ? "停止" : "启动");
                });
    }

    /**
     * 切换全局运行状态
     * @return RunningStateEnum 运行状态枚举
     */
    public synchronized static RunningStateEnum changeRunningState() {
        RUNNING_STATE = RUNNING_STATE.change();
        getActionContext().setHasNext(RunningStateEnum.RUNNING.equals(RUNNING_STATE));
        JButton stateButton = getStateButton();
        stateButton.setText(isRunning() ? "停止" : "启动");
        Optional.ofNullable(SpringUtil.getBean(MainFrame.class))
                .ifPresent(mainFrame -> mainFrame.setTextFieldEnabled(!RunningStateEnum.RUNNING.equals(RUNNING_STATE)));
        return RUNNING_STATE;
    }

    /**
     * 判断是否正在运行
     * @return boolean 是否正在运行
     */
    public static boolean isRunning() {
        return RunningStateEnum.RUNNING.equals(RUNNING_STATE);
    }

    /**
     * 设置全局上下文
     */
    public static void removeActionContext() {
        CONTAINER.remove(APP_CONTEXT);
    }

    /**
     * 获取全局上下文
     * @return actionContext
     */
    public static ActionContext getActionContext() {
        return (ActionContext) CONTAINER.computeIfAbsent(APP_CONTEXT, k -> new ActionContext());
    }

    /**
     * 设置处理器
     * @param process 处理器
     */
    public static void setProcess(LiveWorkbenchService process) {
        CONTAINER.put(PROCESS, process);
    }

    /**
     * 获取处理器
     * @return LiveWorkbenchProcess
     */
    public static LiveWorkbenchService getProcess() {
        return (LiveWorkbenchService) CONTAINER.get(PROCESS);
    }

    /**
     * 设置消息标签
     * @param msgLabel 消息标签
     */
    public static void setMessageLabel(JLabel msgLabel) {
        CONTAINER.put(MESSAGE_LABEL, msgLabel);
    }

    /**
     * 获取消息标签
     * @return JLabel 消息标签
     */
    public static JLabel getMessageLabel() {
        return (JLabel) CONTAINER.get(MESSAGE_LABEL);
    }

    /**
     * 设置状态按钮
     * @param stateButton 状态按钮
     */
    public static void setStateButton(JButton stateButton) {
        CONTAINER.put(STATE_BUTTON, stateButton);
    }

    /**
     * 获取状态按钮
     * @return JButton 状态按钮
     */
    public static JButton getStateButton() {
        return (JButton) CONTAINER.get(STATE_BUTTON);
    }

    /**
     * 获取Caffeine缓存
     * @param key             key
     * @param mappingFunction mapping
     * @return R              R
     */
    public static <R> R getCaffeineCache(String key, Function<String, R> mappingFunction) {
        return (R) CACHE.get(key, mappingFunction);
    }

    public static <R> void putCaffeineCache(String key, R value) {
        if (Objects.isNull(key)) {
            return;
        }
        if (Objects.isNull(value)) {
            CACHE.invalidate(key);
            return;
        }
        CACHE.put(key, value);
    }

    /**
     * 增加开播成功次数
     */
    public static void addStartSuccessCount() {
        CONTAINER.compute(START_SUCCESS_COUNT, (k, v) -> {
            if (Objects.isNull(v)) {
                return new AtomicInteger(1);
            }
            AtomicInteger integer = (AtomicInteger) v;
            integer.incrementAndGet();
            return integer;
        });
    }

    /**
     * 增加开播失败次数
     */
    public static void addStartFailCount() {
        CONTAINER.compute(START_FAIL_COUNT, (k, v) -> {
            if (Objects.isNull(v)) {
                return new AtomicInteger(1);
            }
            AtomicInteger integer = (AtomicInteger) v;
            integer.incrementAndGet();
            return integer;
        });
    }

    /**
     * 获取开播成功次数
     */
    public static int getStartSuccessCount() {
        AtomicInteger integer = (AtomicInteger) CONTAINER.computeIfAbsent(START_SUCCESS_COUNT, k -> new AtomicInteger());
        return integer.get();
    }

    /**
     * 获取开播失败次数
     */
    public static int getStartFailCount() {
        AtomicInteger integer = (AtomicInteger) CONTAINER.computeIfAbsent(START_FAIL_COUNT, k -> new AtomicInteger());
        return integer.get();
    }
}
