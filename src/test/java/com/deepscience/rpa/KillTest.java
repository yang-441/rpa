package com.deepscience.rpa;

import cn.hutool.core.io.FileUtil;
import com.deepscience.rpa.util.process.ProcessKillerUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class KillTest {

    @Test
    public void kill() {
        String processName = FileUtil.getName("D:\\Program Files\\live-anchor-workbench\\淘宝直播主播工作台.exe");
        System.out.println(processName);
        ProcessKillerUtils.kill(processName);
    }

}
