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
    SUCCESS(200, "成功", "Success", ""),
    //请求地址不存在
    NOT_FOUND(404, "很抱歉，你请求的地址不存在！", "我是否和它一样，像过客般匆匆消失...", "⛔"),
    //未处理异常
    FAIL(500, "不好了，内部服务器出错！", "人生的错误可以弥补吗，就像这错误留给你的印象...", "❌"),
    //业务错误
    SERVICE_FAIL(501, "不要慌，逻辑处理失败。", "一定是你的打开方式不对~", "💥"),
    //认证失败
    AUTH_FAIL(503, "这可不行，你没有权限！", "一道不那么厚实的门挡在了你的面前，或许你无法解开它...", "🔒");

    /**
     * 错误代码
     */
    private final Integer code;

    /**
     * 异常时中文描述类型
     */
    private final String type;

    /**
     * 提示信息
     */
    private final String message;

    /**
     * 错误页面的图标
     */
    private final String icon;


    public Integer code() {
        return this.code;
    }

    public String type() {
        return this.type;
    }

    public String message() {
        return this.message;
    }

    public String icon() {
        return this.icon;
    }

    public static ResultCode getByCode(Integer code) {
        for (ResultCode item : ResultCode.values()) {
            if (item.code().equals(code)) {
                return item;
            }
        }
        return ResultCode.FAIL;
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

    public static String getType(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.type;
            }
        }
        return null;
    }
}