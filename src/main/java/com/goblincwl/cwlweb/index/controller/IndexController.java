package com.goblincwl.cwlweb.index.controller;

import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 首页 Controller
 *
 * @author ☪wl
 * @date 2021-04-25 10:35
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final AccessRecordService accessRecordService;

    /**
     * 终端面板显示数据
     *
     * @param request http请求对象
     * @return 数据集
     * @date 2021-04-25 11:05:06
     * @author ☪wl
     */
    @GetMapping("/findTerminalData")
    public Result<Object> findTerminalData(HttpServletRequest request) {
        Map<String, Object> resultMap;
        resultMap = this.accessRecordService.findTerminalData(IpUtils.getIpAddress(request));
        return Result.genSuccess(resultMap, "成功");
    }

    /**
     * 获取天气数据
     *
     * @return 数据集
     * @date 2022/11/16 16:54
     * @author ☪wl
     */
    @GetMapping("/findWeatherData")
    public Result<Object> findWeatherData() {
        Map<String, Object> resultMap;
        resultMap = this.accessRecordService.findWeatherData();
        return Result.genSuccess(resultMap, "成功");
    }

}
