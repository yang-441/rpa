package com.deepscience.rpa.model.login.service;

import com.deepscience.rpa.rpc.api.register.dto.RpaLoginBindDTO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

/**
 * 登录绑定服务
 * @author yangzhuo
 * @date 2025/2/8 19:44
 */
@Validated
public interface LoginBindService {

    /**
     * 登录并绑定
     * @param rpaLoginBindDTO dto
     * @return boolean
     */
    boolean loginAndBind(@Valid RpaLoginBindDTO rpaLoginBindDTO);

    /**
     * 解除绑定
     * @return 执行结果
     */
    boolean unbind();

    /**
     * 校验客户端凭证绑定是否有效
     * @return boolean
     */
    boolean isValidBind();
}
