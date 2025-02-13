package com.deepscience.rpa.rpc.api.register.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * 登录绑定请求DTO
 * @author yangzhuo
 * @date 2025/2/10 11:33
 */
@Data
@Builder
public class RpaLoginBindDTO {
    /**
     * 版本号
     */
    @NotBlank
    private String version;

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
