package com.goblincwl.cwlweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 重定向 Controller
 *
 * @author ☪wl
 * @date 2021-04-23 16:12
 */
@Controller
@RequestMapping("/")
public class RedirectController {

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "/index/index";
    }

    /**
     * 简历
     */
    @GetMapping("/resume")
    public String resume() {
        return "/resume/resume";
    }

}
