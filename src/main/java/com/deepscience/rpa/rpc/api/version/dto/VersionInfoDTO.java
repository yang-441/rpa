package com.deepscience.rpa.rpc.api.version.dto;

import lombok.Data;

/**
 * @author yangzhuo
 * @Description 版本号信息DTO
 * @date 2025/2/13 14:39
 */
@Data
public class VersionInfoDTO {
    /**
     * 当前版本号
     */
    private String version;

    /**
     * 最新版本号
     */
    private String latestVersion;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdate;

    /**
     * 应用下载地址, 需要强制更新时返回
     */
    private String downloadUrl;

    /**
     * 公告信息
     */
    private String notice;

    /**
     * html公告信息
     */
    private String htmlNotice;
}
