package com.deepscience.rpa.common.context;

import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import lombok.Data;

import java.util.List;

/**
 * 自动开播工具上下文
 * @author yangzhuo
 * @date 2025/1/24 14:16
 */
@Data
public class ActionContext {
    /**
     * 屏幕id, 多个显示屏id从0开始
     */
    private Integer screenId;

    /**
     * 直播平台
     */
    private LivePlatform livePlatform;

    /**
     * 直播信息
     */
    private LivePlanDTO livePlan;

    /**
     * 事件类型
     */
    private ActionEventEnum actionEvent;

    /**
     * 执行动作列表
     */
    private List<ActionEnum> actions;

    /**
     * 当前执行事件
     */
    private volatile ActionEnum currentAction;

    /**
     * 回退执行事件, 出现卡点且通过后回退到上一步
     */
    private volatile ActionEnum backAction;

    /**
     * 最大重试次数
     */
    private int maxRetryTime;

    /**
     * 重试次数, 卡点的最大重试次数
     */
    private int retryTime;

    /**
     * 推流url
     */
    private volatile String pushUrl;

    /**
     * 直播间id
     */
    private volatile String liveId;

    /**
     * 截图地址
     */
    private String picPath;

    /**
     * 是否有下一步动作
     */
    private volatile boolean hasNext;

    /**
     * 往下调度
     * @return boolean
     */
    public boolean next() {
        if (isHasNext()) {
            int index = actions.indexOf(currentAction);
            if (index == actions.size() - 1) {
                hasNext = false;
                currentAction = null;
                backAction = null;
                return true;
            }
            if (index < actions.size() - 1) {
                currentAction = actions.get(index + 1);
                return true;
            }
        }
        return false;
    }

    public boolean isHasNext() {
        return hasNext && VariableContainer.isRunning();
    }
}
