package com.deepscience.rpa.model.platform.taobao.service.action;

import com.deepscience.rpa.handler.action.ActionHandler;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;

/**
 * @author yangzhuo
 * @Description 淘宝动作处理器
 * @date 2025/2/12 16:07
 */
public interface TaobaoActionHandler extends ActionHandler {
    /**
     * 获取直播平台
     * @return LivePlatform 直播平台
     */
    @Override
    default LivePlatform getLivePlatform() {
        return LivePlatform.TAOBAO;
    }
}
