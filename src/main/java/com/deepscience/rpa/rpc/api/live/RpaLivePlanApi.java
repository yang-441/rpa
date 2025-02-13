package com.deepscience.rpa.rpc.api.live;

import com.deepscience.rpa.common.config.feign.FeignConfig;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.rpc.api.live.dto.LivePlanDTO;
import com.deepscience.rpa.rpc.constants.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 直播计划API
 * @author yangzhuo
 * @date 2025/2/10 21:05
 */
@Lazy
@Component
@FeignClient(name = "RpaLivePlanApi", url = "${rpa.server.apiUrl}",
        path = ApiConstants.PREFIX, configuration = FeignConfig.class)
public interface RpaLivePlanApi {

    /**
     * 获取未上报的直播计划
     * @return List<LivePlan>
     */
    @GetMapping("/livePlan/unreported")
    CommonResult<List<LivePlanDTO>> getUnreportedLivePlan();

    /**
     * 判断是否存在未上报的直播计划
     * @param plan LivePlanDTO
     * @return CommonResult<Boolean>
     */
    @PostMapping("/livePlan/haveLivePlan")
    CommonResult<Boolean> haveLivePlan(@RequestBody LivePlanDTO plan);
}
