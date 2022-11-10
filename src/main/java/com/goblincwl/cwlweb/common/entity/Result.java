package com.goblincwl.cwlweb.common.entity;

import com.goblincwl.cwlweb.common.enums.ResultCode;
import lombok.*;

import java.io.Serializable;

/**
 * 统一接口返回格式
 *
 * @author ☪wl
 * @date 2020-12-18 17:02
 * @see ResultCode
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Result(ResultCode resultCode) {
        this.setCode(resultCode.getCode());
        this.setMsg(resultCode.getMessage());
    }

    public Result<T> success() {
        this.setCode(ResultCode.SUCCESS.getCode());
        this.setMsg(ResultCode.SUCCESS.getMessage());
        return this;
    }

    public Result<T> success(String msg) {
        Result<T> result = success();
        result.setMsg(msg);
        return result;
    }

    public Result<T> success(T data) {
        Result<T> result = success();
        result.setData(data);
        return result;
    }

    public Result<T> success(T data, String msg) {
        Result<T> result = this.success(msg);
        result.setData(data);
        return result;
    }

    public static Result<Object> genResult(ResultCode resultCode) {
        Result<Object> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMsg(resultCode.getMessage());
        result.setData(resultCode.getType());
        return result;
    }

    public static Result<Object> genSuccess() {
        Result<Object> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMsg(ResultCode.SUCCESS.getMessage());
        return result;
    }

    public static Result<Object> genSuccess(String msg) {
        Result<Object> result = genSuccess();
        result.setMsg(msg);
        return result;
    }

    public static Result<Object> genSuccess(Object data, String msg) {
        Result<Object> result = genSuccess();
        result.setData(data);
        result.setMsg(msg);
        return result;
    }

    public Result<T> fail() {
        this.setCode(ResultCode.FAIL.getCode());
        this.setMsg(ResultCode.FAIL.getMessage());
        return this;
    }

    public Result<T> fail(String msg) {
        Result<T> result = fail();
        result.setMsg(msg);
        return result;
    }

    public Result<T> fail(String msg, Integer code) {
        Result<T> result = fail(msg);
        result.setCode(code);
        return result;
    }

    public static Result<Object> genFail() {
        Result<Object> result = new Result<>();
        result.setCode(ResultCode.FAIL.getCode());
        result.setMsg(ResultCode.FAIL.getMessage());
        result.setData(ResultCode.FAIL.getType());
        return result;
    }

    public static Result<Object> genFail(String msg) {
        Result<Object> result = genFail();
        result.setMsg(msg);
        return result;
    }

    public static Result<Object> genFail(String msg, Integer code) {
        Result<Object> result = genFail(msg);
        result.setCode(code);
        result.setData(ResultCode.getByCode(code).getType());
        return result;
    }
}