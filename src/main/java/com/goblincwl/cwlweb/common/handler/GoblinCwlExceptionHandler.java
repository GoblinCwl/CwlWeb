package com.goblincwl.cwlweb.common.handler;

import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author GoblinCwl
 */
@ControllerAdvice
public class GoblinCwlExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(GoblinCwlExceptionHandler.class);

    /**
     * 全局异常
     *
     * @param exception 异常
     * @return 返回
     * @date 2021-04-28 22:25:01
     * @author ☪wl
     */
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception exception, HttpServletRequest request) {
        LOG.error("发生异常: ", exception);
        if (exception instanceof HttpMessageNotReadableException) {
            return this.goblinCwlExceptionHandler(new GoblinCwlException("请求参数有误"), request);
        }
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return this.goblinCwlExceptionHandler(new GoblinCwlException("不支持" + request.getMethod() + "请求方式"), request);
        }
        request.setAttribute("exception", exception);
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, ResultCode.FAIL.code());
        request.setAttribute("resultCode", ResultCode.FAIL);
        return "forward:/error";
    }

    /**
     * 自定义异常
     *
     * @param exception 异常
     * @return 返回
     * @date 2021-04-28 22:21:05
     * @author ☪wl
     */
    @ExceptionHandler(value = GoblinCwlException.class)
    public String goblinCwlExceptionHandler(GoblinCwlException exception, HttpServletRequest request) {
        LOG.error("业务异常：", exception);
        request.setAttribute("message", exception.getMsg());
        request.setAttribute("exception", exception);
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, exception.getCode());
        ResultCode resultCode = ResultCode.getByCode(exception.getCode());
        request.setAttribute("resultCode", resultCode);
        return "forward:/error";
    }
}