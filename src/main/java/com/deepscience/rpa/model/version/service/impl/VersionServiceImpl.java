package com.deepscience.rpa.model.version.service.impl;

import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.model.version.service.VersionService;
import com.deepscience.rpa.rpc.api.version.VersionApi;
import com.deepscience.rpa.rpc.api.version.dto.VersionInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzhuo
 * @Description 版本号服务
 * @date 2025/2/13 15:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {
    /**
     * 版本号服务
     */
    private final VersionApi versionApi;

    @Override
    public VersionInfoDTO checkVersion(String version) {
        CommonResult<VersionInfoDTO> result = versionApi.checkVersion(version);
        if (result.isSuccess()) {
            return result.getCheckedData();
        } else {
            log.error("版本号校验失败:{}", result.getMsg());
        }
        return null;
    }
}
