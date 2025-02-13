package com.deepscience.rpa.rpc.api.live.dto;

import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.deepscience.rpa.rpc.api.live.enums.PlayStatus;
import com.deepscience.rpa.rpc.api.live.serializer.LocalDateTimeUTCDeserializer;
import com.deepscience.rpa.rpc.api.live.serializer.LocalDateTimeUTCSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 直播计划
 * @author yangzhuo
 * @date 2025/2/10 20:53
 */
@Data
public class LivePlanDTO {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 直播计划编号
     */
    private String playPlanCode;

    /**
     * 直播关联剧本编号
     */
    private String scriptCode;

    /**
     * 脚本id
     */
    private Integer scriptId;

    /**
     * 关联剧本类型
     */
    private String scriptType;

    /**
     * 直播计划任务编号
     */
    private String playPlanTaskCode;

    /**
     * 直播计划名称
     */
    private String playPlanName;

    /**
     * 播出账号
     */
    private String userId;

    /**
     * 直播平台
     */
    private LivePlatform playPlatform;

    /**
     * 直播推流地址
     */
    private String liveUrl;

    /**
     * 推流返回地址
     */
    private String streamUrl;

    /**
     * 直播间ID
     */
    private String liveAccount;

    /**
     * 直播状态
     */
    private PlayStatus playStatus;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */

    private LocalDateTime endTime;

    /**
     * 直播标题
     */
    private String playTitle;

    /**
     * 直播时间
     */

    private LocalDateTime playTime;

    /**
     * 版本
     */
    private String version;

    /**
     * 是否开启交互
     */
    private Integer interactive;

    /**
     * 可见范围 1：所有人可见 2：仅投放至私域
     */
    private Integer visibleRange;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 淘宝上报代码
     */
    private String taobaoReportCode;

    /**
     * 淘宝上报消息
     */
    private String taobaoReportMsg;

    /**
     * 淘宝上报时间
     */
    private LocalDateTime taobaoReportTime;

    /**
     * 失败原因编码
     * 1- 已有直播在进行
     * 2- 未匹配到对应的直播场次
     * 3- 直播账号未登录
     * 5- 其它
     * 6- 正在绑定账号
     */
    private Integer failReasonCode;

    /**
     * 失败原因
     */
    private String failReasonMsg;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 是否有优惠券
     */
    private Integer hasCoupons;

    /**
     * 动作事件
     */
    private ActionEventEnum actionEvent;

    /**
     * 动作事件执行时间
     */
    @JsonSerialize(using = LocalDateTimeUTCSerializer.class)
    @JsonDeserialize(using = LocalDateTimeUTCDeserializer.class)
    private LocalDateTime actionEventTime;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LivePlanDTO that = (LivePlanDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(playPlanCode, that.playPlanCode) && Objects.equals(scriptCode, that.scriptCode) && Objects.equals(scriptId, that.scriptId) && Objects.equals(scriptType, that.scriptType) && Objects.equals(playPlanTaskCode, that.playPlanTaskCode) && Objects.equals(userId, that.userId) && playPlatform == that.playPlatform && Objects.equals(productType, that.productType) && Objects.equals(hasCoupons, that.hasCoupons) && actionEvent == that.actionEvent && Objects.equals(actionEventTime, that.actionEventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playPlanCode, scriptCode, scriptId, scriptType, playPlanTaskCode, userId, playPlatform, productType, hasCoupons, actionEvent, actionEventTime);
    }
}
