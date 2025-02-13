package com.deepscience.rpa.view.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 关闭事件监听
 * @author yangzhuo
 * @date 2025/1/23 18:28
 */
@Slf4j
@Component
public class CloseEventListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
        log.info("window closing...");
        super.windowClosing(e);
    }
}
