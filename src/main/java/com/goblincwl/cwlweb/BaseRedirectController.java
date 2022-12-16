package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础重定向 Controller
 *
 * @author ☪wl
 * @date 2021-05-03 13:43
 */
@Controller
@RequiredArgsConstructor
public class BaseRedirectController {

    private final KeyValueOptionsService keyValueOptionsService;

    /**
     * 首页
     */
    @GetMapping(value = "/")
    public String index(HttpServletRequest request) {
        request.setAttribute("profileUrl", this.keyValueOptionsService.getById("profileUrl").getOptValue());
        request.setAttribute("webMaster", this.keyValueOptionsService.getById("webMaster").getOptValue());
        return "index";
    }

    /**
     * 页面重定向
     *
     * @param request 请求对象
     * @return 视图
     * @date 2021-04-28 00:48:01
     * @author ☪wl
     */
    @GetMapping(value = "/redirect/**", produces = MediaType.TEXT_HTML_VALUE)
    public String redirect(HttpServletRequest request) {
        request.setAttribute("profileUrl", this.keyValueOptionsService.getById("profileUrl").getOptValue());
        request.setAttribute("webMaster", this.keyValueOptionsService.getById("webMaster").getOptValue());
        String prefix = "/redirect/";
        return request.getRequestURI().substring(prefix.length());
    }

}
