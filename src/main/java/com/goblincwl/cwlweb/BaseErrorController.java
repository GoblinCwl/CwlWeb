package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



/**
 * 基础异常 Controller
 *
 * @author ☪wl
 * @date 2021-05-03 2:19
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BaseErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;

    @Autowired
    public BaseErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        super(errorAttributes);
        this.errorProperties = serverProperties.getError();
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    /**
     * HTML页面异常
     *
     * @param request  请求
     * @param response 响应
     * @return 错误页面
     * @date 2021-05-03 02:42:37
     * @author ☪wl
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);

        Map<String, Object> model = new HashMap<>(Collections
                .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML))));
        response.setStatus(status.value());

        //如果是模板找不到，转为404
        String message = (String) model.get("message");
        if (message.contains("template might not exist")) {
            status = HttpStatus.NOT_FOUND;
            model.put("status", HttpStatus.NOT_FOUND.value());
            model.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        }

        //返回枚举
        ResultCode resultCode = (ResultCode) request.getAttribute("resultCode");
        //如果为空，赋值
        if (resultCode == null) {
            //拦截器抛出的认证异常不会被ControllerAdvice处理，此处通过消息判断
            if (message.equals(ResultCode.AUTH_FAIL.getMessage())) {
                //处理认证失败异常
                resultCode = ResultCode.AUTH_FAIL;
                model.put("status", ResultCode.AUTH_FAIL.getCode());
            } else {
                resultCode = ResultCode.FAIL;
            }
        }

        //类型消息
        model.put("typeMessage", resultCode.getType());
        //消息图标
        model.put("messageIcon", resultCode.getIcon());
        //消息
        model.put("message", resultCode.getMessage());

        //需要处理的状态码
        switch (String.valueOf(model.get("status"))) {
            case "404":
                //找不到异常
                model.put("typeMessage", ResultCode.NOT_FOUND.getType());
                model.put("messageIcon", ResultCode.NOT_FOUND.getIcon());
                model.put("message", ResultCode.NOT_FOUND.getMessage());
                break;
            case "501":
                //逻辑异常
                model.put("error", "Logical processing failed");
                break;
            case "503":
                //认证异常
                model.put("error", "Authentication failed");
                break;
            default:
                break;

        }

        model.put("content", model.get("status") + " " + model.get("error") + " " + model.get("messageIcon"));
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("message", model);
    }

    /**
     * API异常
     *
     * @param request 请求
     * @return 错误信息
     * @date 2021-05-03 02:42:55
     * @author ☪wl
     */
    @ResponseBody
    @RequestMapping
    public Result<Object> error(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        String status = String.valueOf(body.get("status"));

        String message = StringUtils.isEmpty((String) request.getAttribute("message"))
                ? (String) body.get("message") : (String) request.getAttribute("message");

        response.reset();
        //如果是模板找不到，或者无请求映射，转为404
        if (message.contains("template might not exist")
                || message.contains("Could not find acceptable representation")) {
            return Result.genResult(ResultCode.NOT_FOUND);
        }

        //返回枚举
        ResultCode resultCode = (ResultCode) request.getAttribute("resultCode");
        //如果为空，赋值
        if (resultCode == null) {
            //拦截器抛出的认证异常不会被ControllerAdvice处理，此处通过消息判断
            if (message.equals(ResultCode.AUTH_FAIL.getMessage())) {
                //处理认证失败异常
                resultCode = ResultCode.AUTH_FAIL;
                status = ResultCode.AUTH_FAIL.getCode().toString();
            } else {
                resultCode = ResultCode.FAIL;
            }
        }

        //需要处理的状态码
        switch (status) {
            case "404":
                message = ResultCode.NOT_FOUND.getMessage();
                break;
            case "501":
                break;
            default:
                message = resultCode.getMessage();
                break;
        }

        return Result.genFail(message,
                StringUtils.isEmpty(status) || "null".equals(status)
                        ? ResultCode.FAIL.getCode() : Integer.parseInt(status));
    }

    protected ErrorAttributeOptions getErrorAttributeOptions(HttpServletRequest request, MediaType mediaType) {
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults();
        if (this.errorProperties.isIncludeException()) {
            options = options.including(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (isIncludeStackTrace(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        if (isIncludeMessage(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (isIncludeBindingErrors(request, mediaType)) {
            options = options.including(ErrorAttributeOptions.Include.BINDING_ERRORS);
        }
        return options;
    }

    @SuppressWarnings("deprecation")
    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeStacktrace()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
            case ON_TRACE_PARAM:
                return getTraceParameter(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeBindingErrors(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeBindingErrors()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getErrorsParameter(request);
            default:
                return false;
        }
    }

    protected boolean isIncludeMessage(HttpServletRequest request, MediaType produces) {
        switch (getErrorProperties().getIncludeMessage()) {
            case ALWAYS:
                return true;
            case ON_PARAM:
                return getMessageParameter(request);
            default:
                return false;
        }
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

}
