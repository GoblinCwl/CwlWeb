package com.goblincwl.cwlweb.common.interceptor;

import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
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

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //注解认证
        boolean isTokenCheck = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (isTokenCheck) {
            TokenCheck tokenCheck = ((HandlerMethod) handler).getMethodAnnotation(TokenCheck.class);
            if (tokenCheck != null && tokenCheck.value()) {
                //需要认证身份
                //验证Token
                if (!ServletUtils.checkToken(request, tokenService)) {
                    //验证不通过
                    throw new GoblinCwlException(ResultCode.AUTH_FAIL);
                }
            }
        }

        String goblinCwlRequestType = request.getHeader("GoblinCwlRequestType");
        //请求头中[不]带有GoblinCwlRequestType=api时，转发至重定向
        if (!"api".equals(goblinCwlRequestType)) {
            request.getRequestDispatcher(request.getContextPath() + "/redirect" + request.getRequestURI()).forward(request, response);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}
