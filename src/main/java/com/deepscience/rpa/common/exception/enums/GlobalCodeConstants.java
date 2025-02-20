package com.deepscience.rpa.common.exception.enums;


import com.deepscience.rpa.common.exception.ResultCode;

/**
 * 全局响应状态码枚举
 * 0-999 系统异常编码保留
 *
 * 虽然说，HTTP 响应状态码作为业务使用表达能力偏弱，但是使用在系统层面还是非常不错的
 * 比较特殊的是，因为之前一直使用 0 作为成功，就不使用 200 啦。
 *
 * @author yangzhuo
 */
public interface GlobalCodeConstants {

    ResultCode SUCCESS = new ResultCode(200, "成功");

    // ========== 客户端错误段 ==========

    ResultCode BAD_REQUEST = new ResultCode(400, "请求参数不正确");
    ResultCode UNAUTHORIZED = new ResultCode(401, "账号未登录");
    ResultCode FORBIDDEN = new ResultCode(403, "没有该操作权限");
    ResultCode NOT_FOUND = new ResultCode(404, "请求未找到");
    ResultCode METHOD_NOT_ALLOWED = new ResultCode(405, "请求方法不正确");
    ResultCode LOCKED = new ResultCode(423, "请求失败，请稍后重试"); // 并发请求，不允许
    ResultCode TOO_MANY_REQUESTS = new ResultCode(429, "请求过于频繁，请稍后重试");

    // ========== 服务端错误段 ==========

    ResultCode INTERNAL_SERVER_ERROR = new ResultCode(500, "系统异常");
    ResultCode NOT_IMPLEMENTED = new ResultCode(501, "功能未实现/未开启");
    ResultCode ERROR_CONFIGURATION = new ResultCode(502, "错误的配置项");

    // ========== 自定义错误段 ==========
    ResultCode REPEATED_REQUESTS = new ResultCode(900, "重复请求，请稍后重试"); // 重复请求
    ResultCode DEMO_DENY = new ResultCode(901, "演示模式，禁止写操作");

    ResultCode UNKNOWN = new ResultCode(999, "未知错误");

    ResultCode FAILED = new ResultCode(1001, "操作失败");

}
