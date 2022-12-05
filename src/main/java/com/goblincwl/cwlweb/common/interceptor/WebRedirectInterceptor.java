package com.goblincwl.cwlweb.common.interceptor;

import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.modules.manager.service.AccessRecordService;
import com.goblincwl.cwlweb.modules.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Web重定向 拦截器
 *
 * @author ☪wl
 * @date 2021-04-28 22:07
 */
@Component
@RequiredArgsConstructor
public class WebRedirectInterceptor implements HandlerInterceptor {

    private final static Logger LOG = LoggerFactory.getLogger(WebRedirectInterceptor.class);

    private final TokenService tokenService;
    private final AccessRecordService accessRecordService;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, Object handler) throws Exception {
        LOG.info("响应请求：" + request.getRequestURI());
        //请求目标是否存在注解
        boolean isAnnotation = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (isAnnotation) {
            TokenCheck tokenCheck = ((HandlerMethod) handler).getMethodAnnotation(TokenCheck.class);
            if (tokenCheck != null && tokenCheck.value()) {
                //需要认证身份
                //验证Token
                if (!ServletUtils.checkToken(request, tokenService)) {
                    //验证不通过
                    throw new GoblinCwlException(ResultCode.AUTH_FAIL);
                }
            }
        } else {
            String requestUri = request.getRequestURI();
            if ("/favicon.ico".equals(requestUri)
                    || requestUri.contains("/app/jrebel")
            ) {
                return true;
            } else {
                //没有注解时，无目标，做转发
                request.getRequestDispatcher(request.getContextPath() + "/redirect" + requestUri).forward(request, response);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        //记录访问日志，缓存用户信息
        this.accessRecordService.saveAccessRecord(IpUtils.getIpAddress(request));
    }

}
