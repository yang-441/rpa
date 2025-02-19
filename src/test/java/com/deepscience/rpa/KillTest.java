package com.deepscience.rpa;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.deepscience.rpa.util.process.CmdUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class KillTest {

    @Test
    public void kill() {
        String processName = FileUtil.getName("D:\\Program Files\\live-anchor-workbench\\淘宝直播主播工作台.exe");
        System.out.println(processName);
        CmdUtils.kill(processName);
    }

    @Test
    public void kill2() {
        for (int i = 0; i < 2; i++) {
            try {
                // 创建 Robot 实例
                Robot robot = new Robot();

                // 按下 Win 键（KeyEvent.VK_WINDOWS 不一定能工作，在某些环境下可能会失效）
                robot.keyPress(KeyEvent.VK_WINDOWS);

                // 按下 D 键
                robot.keyPress(KeyEvent.VK_D);
                robot.keyRelease(KeyEvent.VK_D);

                // 松开 Win 键
                robot.keyRelease(KeyEvent.VK_WINDOWS);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ThreadUtil.safeSleep(1000);
        }
    }

}
