package com.deepscience.rpa.model.platform.taobao.service.action.impl;

import com.deepscience.rpa.common.context.ActionContext;
import com.deepscience.rpa.common.enums.ActionEnum;
import com.deepscience.rpa.model.platform.taobao.constants.TaobaoActionConstants;
import com.deepscience.rpa.model.platform.taobao.enums.TaobaoActionEnum;
import com.deepscience.rpa.model.platform.taobao.service.action.TaobaoActionHandler;
import com.deepscience.rpa.model.platform.taobao.service.wrorkbench.TaobaoLiveWorkbenchService;
import com.deepscience.rpa.util.MsgUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzhuo
 * @Description 淘宝动作执行器 - 获取直播ID
 * @date 2025/2/12 19:23
 */
@Slf4j
@Service(TaobaoActionConstants.DO_GET_LIVE_ID)
@RequiredArgsConstructor
public class TaobaoActionDoGetLiveId implements TaobaoActionHandler {

    /**
     * 直播工作台操作服务
     */
    private final TaobaoLiveWorkbenchService taobaoLiveWorkbenchService;


    @Override
    public ActionEnum getAction() {
        return TaobaoActionEnum.DO_GET_LIVE_ID;
    }

    @Override
    public void handle() {
        MsgUtils.writeInfoMsg("执行获取直播ID...");
        ActionContext actionContext = taobaoLiveWorkbenchService.doGetLiveId();
        if (actionContext.isHasNext()) {
            log.info("获取直播ID动作结束...");
            MsgUtils.writeSuccessMsg("获取直播ID执行结束...");
        } else {
            log.info("获取直播ID动作执行失败...");
            MsgUtils.writeErrorMsg("获取直播ID执行执行失败...");
        }
    }
}
