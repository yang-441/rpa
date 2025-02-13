package com.deepscience.rpa.view.listener;

import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.enums.RunningStateEnum;
import com.deepscience.rpa.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 停止事件监听
 * @author yangzhuo
 * @date 2025/1/26 15:35
 */
@Slf4j
@Component
public class StartStopEventListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        RunningStateEnum state = VariableContainer.changeRunningState();
        MsgUtils.writeInfoMsg(StrUtil.format("切换运行状态, 当前状态[{}]", state.getDesc()));
        log.info("running state: {}", state.getDesc());
    }
}
