package com.deepscience.rpa.model.live.service;

import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;

import java.util.List;

/**
 * 直播计划服务
 * @author yangzhuo
 * @date 2025/2/10 20:45
 */
public interface LivePlanService {
    /**
     * 更新直播计划任务队列
     */
    void updateLivePlanTaskQueue();

    /**
     * 判断当前直播计划是否存在于队列中
     * @param plan 直播计划
     * @return boolean 是否存在
     */
    boolean haveLivePlan(LivePlanDTO plan);

    /**
     * 移除直播计划
     * @param plan 直播计划
     * @return boolean 是否移除成功
     */
    boolean removeLivePlan(LivePlanDTO plan);

    /**
     * 获取当前时间范围内, 未上报的直播计划
     * @return List<LivePlan> 直播计划列表
     */
    List<LivePlanDTO> getUnreportedLivePlan();
}
