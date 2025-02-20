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
 * @Description 通过直播间id检索直播
 * @date 2025/2/20 15:48
 */
@Slf4j
@Service(TaobaoActionConstants.SEARCH_LIVE_BY_ID)
@RequiredArgsConstructor
public class TaobaoActionSearchLiveById implements TaobaoActionHandler {

    /**
     * 直播工作台操作服务
     */
    private final TaobaoLiveWorkbenchService taobaoLiveWorkbenchService;

    @Override
    public ActionEnum getAction() {
        return TaobaoActionEnum.SEARCH_LIVE_BY_ID;
    }

    @Override
    public void handle() {
        MsgUtils.writeInfoMsg("通过直播间id检索直播...");
        ActionContext actionContext = taobaoLiveWorkbenchService.SearchLiveById();
        if (actionContext.isHasNext()) {
            log.info("通过直播间id检索直播动作结束...");
            MsgUtils.writeSuccessMsg("通过直播间id检索直播执行结束...");
        } else {
            log.info("通过直播间id检索直播动作执行失败...");
            MsgUtils.writeErrorMsg("通过直播间id检索直播执行执行失败...");
        }
    }
}
