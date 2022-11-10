package com.goblincwl.cwlweb.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一返回格式数字标识 枚举
 *
 * @author ☪wl
 * @date 2020-12-18 17:06
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    //成功
    SUCCESS(200, "成功", "Success", ""),
    //请求地址不存在
    NOT_FOUND(404, "很抱歉，你请求的地址不存在！", "或许我们所向往的事物，从一开始就不存在.", "⛔"),
    //未处理异常
    FAIL(500, "不好了，内部服务器出错！", "我宁愿犯错，也不愿什么都不做.", "❌"),
    //业务错误
    SERVICE_FAIL(501, "不要慌，逻辑处理失败。", "一定是你的打开方式不对~", "💥"),
    //认证失败
    AUTH_FAIL(503, "这可不行，你没有权限！", "时间被剔除了，你的操作被回溯了，这是！?", "🔒");

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

    public static ResultCode getByCode(Integer code) {
        for (ResultCode item : ResultCode.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return ResultCode.FAIL;
    }

}