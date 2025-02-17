package com.deepscience.rpa.rpc.api.event;

import com.alibaba.fastjson2.JSON;
import com.deepscience.rpa.common.config.feign.FeignConfig;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.rpc.api.event.dto.ActionEventReportDTO;
import com.deepscience.rpa.rpc.api.event.dto.ErrorReportDTO;
import com.deepscience.rpa.rpc.constants.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
     * @param actionEventReportDTO dto
     * @return CommonResult<Boolean> 响应结果
     */
    @PostMapping("/event/report")
    CommonResult<Boolean> actionEventReport(@Valid @RequestBody ActionEventReportDTO actionEventReportDTO);

    /**
     * 执行异常上报
     * @param file           截图信息
     * @param dto            dto
     * @return CommonResult<Boolean> 响应结果
     */
    @PostMapping(value = "/error/report", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonResult<Boolean> errorReport(@RequestPart("file") MultipartFile file,
                                      @RequestParam("dto") String dto);

    /**
     * 执行异常上报
     * @param file 截图信息
     * @param dto  dto
     * @return CommonResult<Boolean>
     */
    default CommonResult<Boolean> errorReport(MultipartFile file,
                                              @Valid ErrorReportDTO dto) {
        String str = JSON.toJSONString(dto);
        return errorReport(file, str);
    }
}
