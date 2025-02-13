package com.deepscience.rpa.model.live.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.model.live.service.LivePlanService;
import com.deepscience.rpa.rpc.api.live.RpaLivePlanApi;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.task.JobScheduling;
import com.deepscience.rpa.task.entity.DelayTaskModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 直播计划服务类
 * @author yangzhuo
 * @date 2025/2/10 20:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LivePlanServiceImpl implements LivePlanService {
    /**
     * 直播计划任务队列
     */
    private static final LinkedBlockingDeque<LivePlanDTO> LIVE_PLAN_TASK_QUEUE = new LinkedBlockingDeque<>();

    /**
     * 直播计划API
     */
    private final RpaLivePlanApi rpaLivePlanApi;

    @Override
    public void updateLivePlanTaskQueue() {
        List<LivePlanDTO> livePlanDTOS;
        try {
            livePlanDTOS = getUnreportedLivePlan();
        } catch (Exception e) {
            log.error("更新直播计划队列执行异常", e);
            return;
        }
        log.info("[更新直播计划队列], 时间: {}, 获取当前直播计划: {}", DateUtil.now(), livePlanDTOS);
        synchronized (this) {
            // 更新队列
            LIVE_PLAN_TASK_QUEUE.clear();
            // 加入定时任务
            if (CollUtil.isNotEmpty(livePlanDTOS)) {
                LIVE_PLAN_TASK_QUEUE.addAll(livePlanDTOS);
                livePlanDTOS.stream()
                        .filter(plan -> Objects.nonNull(plan.getActionEvent())
                                && Objects.nonNull(plan.getActionEventTime())
                        ).forEach(plan -> {
                            DelayTaskModel<LivePlanDTO> startTask = new DelayTaskModel<>(plan, plan.getActionEventTime());
                            JobScheduling.putDelayTask(startTask);
                        });
            }
        }
    }

    @Override
    public boolean haveLivePlan(LivePlanDTO plan) {
        if (Objects.isNull(plan)) {
            return false;
        }
        boolean have;
        synchronized (this) {
            have = LIVE_PLAN_TASK_QUEUE.contains(plan);
        }
        if (have) {
            CommonResult<Boolean> res = rpaLivePlanApi.haveLivePlan(plan);
            have = res.isSuccess() && Boolean.TRUE.equals(res.getData());
        }
        return have;
    }

    @Override
    public boolean removeLivePlan(LivePlanDTO plan) {
        if (Objects.isNull(plan)) {
            return false;
        }
        synchronized (this) {
            return LIVE_PLAN_TASK_QUEUE.remove(plan);
        }
    }

    @Override
    public List<LivePlanDTO> getUnreportedLivePlan() {
        CommonResult<List<LivePlanDTO>> resp = rpaLivePlanApi.getUnreportedLivePlan();
        return resp.isSuccess() ? resp.getData() : new ArrayList<>();
    }

}
