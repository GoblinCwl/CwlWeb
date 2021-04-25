package com.goblincwl.cwlweb.common.exception;

import com.goblincwl.cwlweb.common.enums.ResultCode;
import lombok.Getter;

/**
 * 统一异常处理自定义异常.
 *
 * @author ☪wl
 * @date 2020-12-18 17:18
 */
@Getter
public class GoblinCwlException extends RuntimeException {

    /**
     * 错误代码
     */
    private final Integer code;

    /**
     * 使用枚举异常.
     *
     * @param resultCode 状态码枚举
     * @date 2020-12-18 17:20
     * @author ☪wl
     */
    public GoblinCwlException(ResultCode resultCode) {
        super(resultCode.message());
        this.code = resultCode.code();
    }

    /**
     * 使用自定异常.
     *
     * @param code 错误代码
     * @param msg  提示消息
     * @date 2020-12-18 17:20
     * @author ☪wl
     */
    public GoblinCwlException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    /**
     * 业务逻辑异常抛出
     *
     * @param msg 提示消息
     * @date 2020-12-22 22:05:33
     * @author ☪wl
     */
    public GoblinCwlException(String msg) {
        super(msg);
        this.code = ResultCode.SERVICE_FAIL.code();
    }

}
