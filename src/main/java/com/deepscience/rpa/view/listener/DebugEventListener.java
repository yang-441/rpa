package com.deepscience.rpa.view.listener;

import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.model.event.service.ActionEventReportService;
import com.deepscience.rpa.model.live.service.LivePlanService;
import com.deepscience.rpa.rpc.api.event.enums.ActionEventEnum;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.rpc.api.live.enums.LivePlatform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 调试事件监听
 * @author yangzhuo
 * @date 2025/1/23 17:46
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DebugEventListener implements ActionListener {

    private final LivePlanService livePlanService;

    private final ActionEventReportService actionEventReportService;

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("start running...");
        LivePlanDTO livePlanDTO = new LivePlanDTO();
        livePlanDTO.setId(1L);
        livePlanDTO.setPlayPlanCode("1");
        livePlanDTO.setPlayPlatform(LivePlatform.TAOBAO);
        livePlanDTO.setActionEvent(ActionEventEnum.START_LIVING);
        VariableContainer.getActionContext().setScreenId(0);
        actionEventReportService.errorReportAsync(livePlanDTO);
        // livePlanService.updateLivePlanTaskQueue();
    }
}
