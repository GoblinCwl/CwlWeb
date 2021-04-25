package com.goblincwl.cwlweb.common.enums;

import lombok.AllArgsConstructor;

/**
 * 统一返回格式数字标识 枚举
 *
 * @author ☪wl
 * @date 2020-12-18 17:06
 */
@AllArgsConstructor
public enum ResultCode {
    //成功
    SUCCESS(200, "成功"),
    //请求地址不存在
    NOT_FOUND(404, "请求地址不存在"),
    //未处理异常
    FAIL(500, "服务调用失败，请联系管理员"),
    //业务错误
    SERVICE_FAIL(501, "失败");

    /**
     * 错误代码
     */
    private final Integer code;
    /**
     * 提示信息
     */
    private final String message;

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }
}