package com.deepscience.rpa.common.constants;

import cn.hutool.core.io.FileUtil;

/**
 * 应用程序文件常量
 * @author yangzhuo
 * @date 2025/1/23 17:38
 */
public class AppFileConstants {
    /**
     * 项目基础路径
     */
    public static final String BATE_PATH = FileUtil.getUserHomePath() + FileUtil.FILE_SEPARATOR + "autoLive";

    /**
     * config路径
     */
    public static final String CONFIG_PATH = BATE_PATH + FileUtil.FILE_SEPARATOR + "system-conf.properties";
}
