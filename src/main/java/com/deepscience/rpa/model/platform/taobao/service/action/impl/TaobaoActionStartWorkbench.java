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
 * 淘宝动作执行器
 * @author yangzhuo
 * @date 2025/1/26 16:31
 */
@Slf4j
@Service(TaobaoActionConstants.START_WORKBENCH)
@RequiredArgsConstructor
public class TaobaoActionStartWorkbench implements TaobaoActionHandler {

    /**
     * 直播工作台操作服务
     */
    private final TaobaoLiveWorkbenchService taobaoLiveWorkbenchService;

    @Override
    public ActionEnum getAction() {
        return TaobaoActionEnum.START_WORKBENCH;
    }

    @Override
    public void handle() {
        // start listening
        MsgUtils.writeInfoMsg("执行启动工作台...");
        ActionContext actionContext = taobaoLiveWorkbenchService.startWorkbench();
        if (actionContext.isHasNext()) {
            log.info("执行启动工作台动作结束...");
            MsgUtils.writeSuccessMsg("执行启动工作台动作结束...");
        } else {
            log.info("执行启动工作台动作执行失败...");
            MsgUtils.writeErrorMsg("执行启动工作台动作执行失败...");
        }
    }
}
