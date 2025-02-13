package com.deepscience.rpa.common.exception;

import lombok.Data;

/**
 * 响应状态码对象
 * @author yangzhuo
 */
@Data
public class ResultCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String msg;

    public ResultCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

}
