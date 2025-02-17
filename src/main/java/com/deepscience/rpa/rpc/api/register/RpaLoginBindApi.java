package com.deepscience.rpa.rpc.api.register;

import com.deepscience.rpa.common.config.feign.FeignConfig;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.rpc.api.register.dto.RpaLoginBindDTO;
import com.deepscience.rpa.rpc.constants.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 自动化工具注册绑定API
 * @author yangzhuo
 * @date 2025/2/10 11:24
 */
@Lazy
@Component
@FeignClient(name = "RpaLoginBindApi", url = "${rpa.server.apiUrl}",
        path = ApiConstants.PREFIX, configuration = FeignConfig.class )
public interface RpaLoginBindApi {

    /**
     * 通过账号密码注册绑定
     * @param rpaLoginBindDTO dto
     * @return CommonResult<String> 响应结果
     */
    @PostMapping("/register")
    CommonResult<String> register(@RequestBody RpaLoginBindDTO rpaLoginBindDTO);

    /**
     * 通过客户端凭证解绑
     * @return CommonResult<Boolean>
     */
    @DeleteMapping("/unbind")
    CommonResult<Boolean> unbind();

    /**
     * 判断客户端凭证是否有效
     * @return CommonResult<Boolean>
     */
    @GetMapping("/isValidBind")
    CommonResult<Boolean> isValidBind(@RequestHeader("Authorization") String clientCredentials);
}
