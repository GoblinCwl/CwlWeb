package com.goblincwl.cwlweb.common.entity;

import com.goblincwl.cwlweb.common.enums.ResultCode;
import lombok.*;

/**
 * 自定义异常
 *
 * @author ☪wl
 * @date 2021-04-28 22:12
 */
@Getter
@Setter
public class GoblinCwlException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer code;
    /**
     * 错误信息
     */
    protected String msg;

    public GoblinCwlException() {
        super(ResultCode.SERVICE_FAIL.message());
        this.code = ResultCode.SERVICE_FAIL.code();
        this.msg = ResultCode.SERVICE_FAIL.message();
    }

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
        this.msg = resultCode.message();
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
        this.msg = msg;
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
        this.msg = msg;
    }
}
