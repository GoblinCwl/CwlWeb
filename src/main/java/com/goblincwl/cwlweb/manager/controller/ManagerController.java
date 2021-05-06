package com.goblincwl.cwlweb.manager.controller;

import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Manager 管理模块 Controller
 *
 * @author ☪wl
 * @date 2021-05-03 14:22
 */
@RestController
@RequestMapping(ManagerController.MODULE_PREFIX)
@RequiredArgsConstructor
public class ManagerController {

    public final static String MODULE_PREFIX = "/manager";
    private final KeyValueOptionsService keyValueOptionsService;
    private final TokenService tokenService;

    /**
     * 管理员登陆，获取token
     *
     * @param password 登录密码
     * @return 认证Token
     * @date 2021-05-03 14:49:59
     * @author ☪wl
     */
    @PostMapping("/login")
    public Result<Object> login(HttpServletRequest request,
                                String password) {
        if (StringUtils.isNotEmpty(password)) {

            KeyValueOptions loginPassword = keyValueOptionsService.getById("loginPassword");
            //匹配密码
            if (password.equals(loginPassword.getOptValue())) {
                //使用UUID作为token
                String token = this.tokenService.genToken(IpUtils.getIpAddress(request));
                return Result.genSuccess(token, "登陆成功");
            } else {
                throw new GoblinCwlException("登陆失败，密码错误。");
            }
        }
        throw new GoblinCwlException("登陆失败，密码不能为空。");
    }

    /**
     * 登出
     *
     * @param request 请求对象
     * @return 反馈
     * @date 2021-05-04 23:35:24
     * @author ☪wl
     */
    @GetMapping("/logout")
    public Result<Object> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        this.tokenService.clearToken(token);
        return Result.genSuccess();
    }
}
