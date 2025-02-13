package com.deepscience.rpa.model.login.service.impl;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.deepscience.rpa.common.config.AppConfig;
import com.deepscience.rpa.common.container.VariableContainer;
import com.deepscience.rpa.common.pojo.CommonResult;
import com.deepscience.rpa.model.login.service.LoginBindService;
import com.deepscience.rpa.rpc.api.register.RpaLoginBindApi;
import com.deepscience.rpa.rpc.api.register.dto.RpaLoginBindDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 登录绑定服务
 * @author yangzhuo
 * @date 2025/2/8 20:18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginBindServiceImpl implements LoginBindService {
    /**
     * APP配置文件
     */
    private final AppConfig appConfig;

    /**
     * 登录绑定api
     */
    private final RpaLoginBindApi rpaLoginBindApi;

    @Override
    public boolean loginAndBind(RpaLoginBindDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        log.info("version: {}, 用户名: {}, 密码: {}",
                dto.getVersion(), username, DesensitizedUtil.password(password));
        CommonResult<String> register = rpaLoginBindApi.register(dto);
        if (register.isSuccess() && StrUtil.isNotEmpty(register.getData())) {
            String clientCredentials = register.getData();
            log.info("登录绑定成功, 用户名:{}, clientCredentials: {}", username, clientCredentials);
            appConfig.setUsername(username);
            appConfig.setClientCredentials(clientCredentials);
            return true;
        }
        return false;
    }

    @Override
    public boolean unbind() {
        CommonResult<Boolean> resp = rpaLoginBindApi.unbind();
        return resp.isSuccess() && Boolean.TRUE.equals(resp.getData());
    }

    @Override
    public boolean isValidBind() {
        String clientCredentials = appConfig.getClientCredentials();
        if(StrUtil.isBlank(clientCredentials)) {
            return false;
        }
        return VariableContainer.getCaffeineCache(clientCredentials, this::callIsValidBind);
    }

    /**
     * 验证绑定
     * @return boolean
     */
    private boolean callIsValidBind(String clientCredentials) {
        CommonResult<Boolean> resp = rpaLoginBindApi.isValidBind(clientCredentials);
        boolean isValidBind = resp.isSuccess() && Boolean.TRUE.equals(resp.getData());
        // 无效绑定, 清空本地配置信息
        if (!isValidBind) {
            appConfig.setClientCredentials(null);
        }
        return isValidBind;
    }

}
