package com.goblincwl.cwlweb.index.controller;

import com.alibaba.fastjson.JSONObject;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private final AccessRecordService accessRecordService;

    /**
     * 首页看板
     *
     * @param request http请求对象
     * @return 数据集
     * @date 2021-04-25 11:05:06
     * @author ☪wl
     */
    @GetMapping("/findTerminalData")
    public Result<Object> findTerminalData(HttpServletRequest request) {
        Map<String, Object> resultMap;
        //先从Redis拿缓存
        String redisKey = "indexDashboardData";
        String indexDashboardDataStr = (String) redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(indexDashboardDataStr)) {
            //终端数据
            resultMap = this.accessRecordService.findTerminalData(IpUtils.getIpAddress(request));
            //天气数据
            resultMap.putAll(this.accessRecordService.findWeatherData());
            //编程语言工时数据
            resultMap.putAll(this.accessRecordService.findWorkLanguageData());
            //工时数据
            resultMap.putAll(this.accessRecordService.findWorkTimeData());

            //存到Redis
            redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(resultMap));
            //设置有效期
            redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
        } else {
            resultMap = JSONObject.parseObject(indexDashboardDataStr, Map.class);
        }
        return Result.genSuccess(resultMap, "成功");
    }

}
