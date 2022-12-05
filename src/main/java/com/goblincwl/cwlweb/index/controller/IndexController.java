package com.goblincwl.cwlweb.index.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.entity.AccessRecord;
import com.goblincwl.cwlweb.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private final static Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private final AccessRecordService accessRecordService;

    private final KeyValueOptionsService keyValueOptionsService;

    @PostMapping("/httpRequest")
    public Result<JSONObject> httpRequest(@RequestParam Map<String, Object> param) {
        JSONObject jsonObject = null;

        String url = (String) param.get("url");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // 获取响应实体
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Result<JSONObject>().success(jsonObject, "成功");
    }

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
        Map<String, Object> resultMap = new HashMap<>();
        //先从Redis拿缓存
        String redisKey = "indexDashboardData";
        String indexDashboardDataStr = (String) redisTemplate.opsForValue().get(redisKey);
        boolean isRedis = !StringUtils.isEmpty(indexDashboardDataStr);
        if (!isRedis) {
            //天气数据
            resultMap.putAll(this.accessRecordService.findWeatherData());
            //编程语言工时数据
            resultMap.putAll(this.accessRecordService.findWorkLanguageData());
            //工时数据
            resultMap.putAll(this.accessRecordService.findWorkTimeData());

            boolean hasHttpError = resultMap.get("hasHttpError") != null && (Boolean) resultMap.get("hasHttpError");
            if (!hasHttpError) {
                //无异常，存到Redis
                redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(resultMap));
                //设置有效期
                redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
            }

        } else {
            resultMap = JSONObject.parseObject(indexDashboardDataStr, Map.class);
        }

        //终端数据,另起缓存
        resultMap.putAll(this.accessRecordService.findTerminalData(IpUtils.getIpAddress(request)));
        //清楚旧的浇水时间
        if (isRedis) {
            resultMap.remove("lastWateringTime");
        }

        //金盏花浇水次数，不缓存
        resultMap.put("numberOfWatering", this.keyValueOptionsService.getById("numberOfWatering").getOptValue());
        //金盏花上次浇水时间
        if (resultMap.get("lastWateringTime") == null) {
            AccessRecord accessRecord = this.accessRecordService.getOne(new LambdaQueryWrapper<AccessRecord>().eq(AccessRecord::getIpAddress, IpUtils.getIpAddress(request)));
            resultMap.put("lastWateringTime", accessRecord.getLastWateringTime());
        }
        return Result.genSuccess(resultMap, "成功");
    }

    /**
     * 金盏花-浇水
     *
     * @param request 请求体
     * @return 反馈
     * @date 2022/11/19 8:58
     * @author ☪wl
     */
    @GetMapping("/watering")
    public Result<String> watering(HttpServletRequest request) throws ParseException {
        //查询IP地址浇水情况
        AccessRecord accessRecord = this.accessRecordService.getOne(new LambdaQueryWrapper<AccessRecord>().eq(AccessRecord::getIpAddress, IpUtils.getIpAddress(request)));
        if (accessRecord != null) {
            Date lastWateringTime = accessRecord.getLastWateringTime();
            if (lastWateringTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //今天
                Date today = sdf.parse(sdf.format(new Date()));
                //上次浇水时间
                Date lastWaterDay = sdf.parse(sdf.format(lastWateringTime));
                if (lastWaterDay.compareTo(today) >= 0) {
                    throw new GoblinCwlException("今天已经浇过水啦！");
                }
            }
        } else {
            throw new GoblinCwlException("数据异常，请刷新后重试！");
        }

        //默认消息
        String message = this.keyValueOptionsService.getById("defaultFlowerMessage").getOptValue();
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

                //更新浇水信息
                accessRecord.setLastWateringTime(new Date());
                this.accessRecordService.updateById(accessRecord);
                String wateringKey = "numberOfWatering";
                KeyValueOptions keyValueOptions = this.keyValueOptionsService.getById(wateringKey);
                int number = Integer.parseInt(keyValueOptions.getOptValue());
                keyValueOptions.setOptValue(String.valueOf(number + 1));
                this.keyValueOptionsService.updateById(keyValueOptions);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
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
