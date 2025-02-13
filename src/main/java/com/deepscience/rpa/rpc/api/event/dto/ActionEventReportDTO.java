package com.deepscience.rpa.rpc.api.event.dto;

import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 执行事件上报DTO
 * @author yangzhuo
 * @date 2025/2/11 17:07
 */
@Data
public class ActionEventReportDTO {
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
     * 直播间ID
     */
    private String liveAccount;

    /**
     * 直播推流地址
     */
    private String liveUrl;

    /**
     * 执行动作事件
     */
    @NotNull
    private ActionEventEnum actionEvent;

    /**
     * 图片地址
     */
    private String picUrl;
}
