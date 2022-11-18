package com.goblincwl.cwlweb.index.controller;

import com.alibaba.fastjson.JSONObject;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
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

    @GetMapping("/flowerSay")
    public Result<String> flowerSay() {
        String message = "唯独对你，我没有办法开口...";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("v1.hitokoto.cn")
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    message = jsonObject.getString("hitokoto");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new Result<String>().success(message, "成功");

    }

}
