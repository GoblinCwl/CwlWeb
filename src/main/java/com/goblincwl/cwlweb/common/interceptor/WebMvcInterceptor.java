package com.goblincwl.cwlweb.common.interceptor;

import com.goblincwl.cwlweb.common.entity.GoblinCwlConfig;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.enums.ResultCode;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    private final GoblinCwlConfig goblinCwlConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String goblinCwlRequestType = request.getHeader("GoblinCwlRequestType");
        //验证Token
        if (!ServletUtils.checkToken(request, this.tokenService)) {
            //验证不通过
            throw new GoblinCwlException(ResultCode.AUTH_FAIL);
        }

        //API请求白名单
        boolean checkApi = true;
        List<String> apiRequestWhiteList = this.goblinCwlConfig.getApiRequestWhiteList();
        String requestUri = request.getRequestURI();
        for (String whiteUri : apiRequestWhiteList) {
            if (requestUri.startsWith(whiteUri)) {
                checkApi = false;
                break;
            }
        }
        if (checkApi) {
            //请求头中[不]带有GoblinCwlRequestType=api时，转发至重定向
            if (!"api".equals(goblinCwlRequestType)) {
                request.getRequestDispatcher(request.getContextPath() + "/redirect" + requestUri).forward(request, response);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}
