package com.goblincwl.cwlweb.common.interceptor;

import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebMVC 拦截器
 *
 * @author ☪wl
 * @date 2021-04-28 22:07
 */
@Component
@RequiredArgsConstructor
public class WebMvcInterceptor implements HandlerInterceptor {

    private final TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String goblinCwlRequestType = request.getHeader("GoblinCwlRequestType");
        //验证Token
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            //请求头未携带token，从cookie中获取
            Cookie[] cookies = request.getCookies();
            if (cookies != null){
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (!this.tokenService.checkToken(token, IpUtils.getIpAddress(request))) {
            //验证不通过
            throw new GoblinCwlException(ResultCode.AUTH_FAIL);
        }
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
