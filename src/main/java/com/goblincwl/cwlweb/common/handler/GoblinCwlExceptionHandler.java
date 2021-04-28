package com.goblincwl.cwlweb.common.handler;

import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


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
    @ResponseBody
    public Result<Object> exceptionHandler(Exception exception) {
        LOG.error("发生异常: ", exception);
        if (exception instanceof HttpMessageNotReadableException) {
            return Result.genFail("请求参数有误", ResultCode.FAIL.code());
        }
        return Result.genResult(ResultCode.FAIL);
    }

    /**
     * 自定义异常
     *
     * @param e 异常
     * @return 返回
     * @date 2021-04-28 22:21:05
     * @author ☪wl
     */
    @ExceptionHandler(value = GoblinCwlException.class)
    @ResponseBody
    public Result<Object> bizExceptionHandler(GoblinCwlException e) {
        LOG.error("业务异常: {}", e.getMsg());
        return Result.genFail(e.getMsg(), e.getCode());
    }
}