package com.deepscience.rpa.rpc.api.event;

import com.deepscience.rpa.common.config.feign.FeignConfig;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.rpc.api.event.dto.ActionEventReportDTO;
import com.deepscience.rpa.rpc.constants.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 执行事件上报API
 * @author yangzhuo
 * @date 2025/2/10 21:05
 */
@Lazy
@Validated
@Component
@FeignClient(name = "ActionEventReportApi", url = "${rpa.server.apiUrl}",
        path = ApiConstants.PREFIX, configuration = FeignConfig.class)
public interface ActionEventReportApi {

    /**
     * 执行事件上报
     * @param rpaLoginBindDTO dto
     * @return CommonResult<String> 响应结果
     */
    @PostMapping("/event/report")
    CommonResult<Boolean> actionEventReport(@Valid @RequestBody ActionEventReportDTO rpaLoginBindDTO);
}
