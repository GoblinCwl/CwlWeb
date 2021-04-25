package com.goblincwl.cwlweb.common.exception;

import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;



/**
 * 统一异常处理 异常处理器.
 * <p>当程序异常时，通过此处理器进行异常处理.<br>
 * <p>包括404 NOT FOUND错误<br>
 *
 * @author ☪wl
 * @date 2020-12-18 17:21
 */
@ResponseBody
@ControllerAdvice
public class GoblinCwlExceptionHandle {

    /**
     * 全局异常处理.
     *
     * @param exception 异常对象
     * @return 处理异常的返回
     * @date 2020-12-19 11:39
     * @author ☪wl
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handle(Exception exception) {
        if (exception instanceof NoHandlerFoundException) {
            return Result.genFail(ResultCode.NOT_FOUND.message(), ResultCode.NOT_FOUND.code());
        } else if (exception instanceof HttpMessageNotReadableException) {
            return Result.genFail("请求参数有误", ResultCode.FAIL.code());
        }
        exception.printStackTrace();
        return Result.genFail(ResultCode.FAIL.message(), ResultCode.FAIL.code());
    }

    /**
     * 针对 GoblinCwlException 的异常处理.
     *
     * @param goblinCwlException 异常对象
     * @return 处理异常的返回
     * @date 2020-12-18 17:23
     * @author ☪wl
     */
    @ExceptionHandler(GoblinCwlException.class)
    public Result<Object> handle(GoblinCwlException goblinCwlException) {
        return Result.genFail(goblinCwlException.getMessage(), goblinCwlException.getCode());
    }
}
