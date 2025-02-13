package com.deepscience.rpa.rpc.api.version;

import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.rpc.api.version.dto.VersionInfoDTO;
import com.deepscience.rpa.rpc.constants.ApiConstants;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yangzhuo
 * @Description 版本号API
 * @date 2025/2/13 14:37
 */
@Lazy
@Validated
@Component
@FeignClient(name = "VersionApi", url = "${rpa.server.apiUrl}",
        path = ApiConstants.PREFIX)
public interface VersionApi {

    /**
     * 校验版本号信息
     * @param version 版本号
     * @return VersionInfoDTO 版本号信息
     */
    @GetMapping("/version/check")
    CommonResult<VersionInfoDTO> checkVersion(@NotBlank @RequestParam("version") String version);
}
