package com.deepscience.rpa.model.platform.taobao.service.wrorkbench;

import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import com.deepscience.rpa.service.LiveWorkbenchService;

/**
 * @author yangzhuo
 * @Description 淘宝直播工作台动作执行服务
 * @date 2025/2/12 16:29
 */
public interface TaobaoLiveWorkbenchService extends LiveWorkbenchService {
    /**
     * 获取直播平台
     * @return LivePlatform 直播平台
     */
    @Override
    default LivePlatform getLivePlatform() {
        return LivePlatform.TAOBAO;
    }
}
