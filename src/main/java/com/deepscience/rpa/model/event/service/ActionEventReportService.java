package com.deepscience.rpa.model.event.service;

import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;

/**
 * 执行事件上报服务
 * @author yangzhuo
 * @date 2025/2/11 17:17
 */
public interface ActionEventReportService {
    /**
     * 上报直播计划信息
     * @param livePlan 直播计划信息
     */
    boolean reportInfo(LivePlanDTO livePlan);

    /**
     * 重试上报直播计划信息
     */
    void retryReport();

    /**
     * 上报失败信息, 同步截屏, 同步上报
     * @param livePlan 直播计划信息
     */
    void errorReport(LivePlanDTO livePlan);

    /**
     * 异步上报失败信息, 同步截屏, 异步上报
     * @param livePlan 直播计划信息
     */
    void errorReportAsync(LivePlanDTO livePlan);
}
