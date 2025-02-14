package com.deepscience.rpa.common.constants;

/**
 * @author yangzhuo
 * @Description HTML模板常量
 * @date 2025/2/13 15:39
 */
public class HtmlTemplateConstants {
    /**
     * 服务器异常模板
     */
    public static final String SERVER_ERROR_TEMPLATE = "<html>" +
            "<div style='text-align:center;'>" +
            "<h2>服务器异常</h2>" +
            "<p>服务器出现异常，请稍后再试。</p>" +
            "</div>" +
            "</html>";

    /**
     * 中文目录异常模板
     */
    public static final String RUNTIME_ERROR_HAS_CHINESE = "<html>" +
            "<div style='text-align:center;'>" +
            "<h2>目录环境异常</h2>" +
            "<p>程序运行路径不能包含中文</p>" +
            "</div>" +
            "</html>";

    /**
     * 更新公告模板
     */
    public static final String UPDATE_NOTICE_TEMPLATE = "<html>" +
            "<div style='text-align:center;'>" +
            "<h2>强制更新通知</h2>" +
            "<p><strong>当前版本：</strong>{}</p>" +
            "<p><strong>最新版本：</strong>{}</p>" +
            "<p>{}</p>" +
            "<p>请点击以下链接下载最新版本：</p>" +
            "<p><a href='{}' style='text-decoration:none;'>下载地址</a></p>" +
            "</div>" +
            "</html>";
}
