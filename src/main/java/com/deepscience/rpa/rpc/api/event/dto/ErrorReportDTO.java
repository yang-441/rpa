package com.deepscience.rpa.rpc.api.event.dto;

import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author yangzhuo
 * @Description 异常上报DTO
 * @date 2025/2/17 15:18
 */
@Data
public class ErrorReportDTO {
    /**
     * 直播计划id
     */
    @NotNull
    private Long planId;

    /**
     * 直播平台
     */
    @NotNull
    private LivePlatform playPlatform;

    /**
     * 直播计划编号
     */
    @NotNull
    private String playPlanCode;

    /**
     * 执行动作事件
     */
    @NotNull
    private ActionEventEnum actionEvent;
}
