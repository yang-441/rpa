package com.deepscience.rpa.view.listener;

import com.deepscience.rpa.model.live.service.LivePlanService;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        log.info("start running...");
        livePlanService.updateLivePlanTaskQueue();
    }
}
