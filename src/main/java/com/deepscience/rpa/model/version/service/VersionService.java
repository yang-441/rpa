package com.deepscience.rpa.model.version.service;

import com.deepscience.rpa.rpc.api.version.dto.VersionInfoDTO;

/**
 * @author yangzhuo
 * @Description 版本号服务
 * @date 2025/2/13 15:05
 */
public interface VersionService {
    /**
     * 检查版本号
     * @param version 当前版本号
     * @return VersionInfoDTO
     */
    VersionInfoDTO checkVersion(String version);
}
