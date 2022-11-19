package com.goblincwl.cwlweb.manager.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.common.utils.NickNameUtils;
import com.goblincwl.cwlweb.manager.entity.AccessRecord;
import com.goblincwl.cwlweb.manager.mapper.AccessRecordMapper;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 访问记录 Service
 *
 * @author ☪wl
 * @date 2021-04-24 18:34
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AccessRecordService extends ServiceImpl<AccessRecordMapper, AccessRecord> {

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    private final AccessRecordMapper accessRecordMapper;

    private final KeyValueOptionsService keyValueOptionsService;

    /**
     * 终端面板显示数据
     *
     * @param ipAddress 访问IP地址
     * @return 数据集
     * @date 2021-04-25 11:05:06
     * @author ☪wl
     */
    public Map<String, Object> findTerminalData(String ipAddress) {
        Map<String, Object> resultMap = new HashMap<>();
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setIpAddress(ipAddress);

        AccessRecord accessRecordResult = this.accessRecordMapper.selectOne(new QueryWrapper<>(accessRecord));
        if (accessRecordResult == null) {
            //保存访客信息
            accessRecord.setNickName(NickNameUtils.randomName(2));
            accessRecord.setAccessTime(new Date());
            accessRecord.setAccessCount(1);
            int saveOneResult = this.accessRecordMapper.insert(accessRecord);
            if (saveOneResult < 1) {
                //TODO
                throw new RuntimeException("新增失败");
            }
        } else {
            accessRecord = accessRecordResult;
            //记录访问时间和上次访问时间
            accessRecord.setLastAccessTime(accessRecord.getAccessTime());
            accessRecord.setAccessTime(new Date());
            //累加访问次数
            accessRecord.setAccessCount(accessRecord.getAccessCount() + 1);
            int updateOneResult = this.accessRecordMapper.updateById(accessRecord);
            if (updateOneResult < 1) {
                //TODO
                throw new RuntimeException("修改失败");
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //本次访问
        resultMap.put("accessTime", sdf.format(accessRecord.getAccessTime()));
        //上次访问
        resultMap.put("lastAccessTime", accessRecord.getLastAccessTime() == null ? "首次访问" : sdf.format(accessRecord.getLastAccessTime()));
        //总访问次数
        resultMap.put("accessCount", this.accessRecordMapper.selectAccessCount());
        //总访问IP数
        resultMap.put("accessIpCount", accessRecord.getId() == null ? this.accessRecordMapper.selectAccessIpCount() : accessRecord.getId());
        //昵称
        resultMap.put("nickName", accessRecord.getNickName());
        //IP地址
        resultMap.put("ipAddress", accessRecord.getIpAddress());
        //上次浇水时间
        resultMap.put("lastWateringTime", accessRecord.getLastWateringTime());
        return resultMap;
    }

    public Map<String, Object> findWeatherData() {
        Map<String, Object> resultMap = new HashMap<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("restapi.amap.com")
                    .setPath("/v3/weather/weatherInfo")
                    .setParameter("key", this.keyValueOptionsService.getById("AmapApiKey").getOptValue())
                    //TODO 动态城市编码
                    .setParameter("city", "320200")
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    JSONObject lives = jsonObject.getJSONArray("lives").getJSONObject(0);
                    resultMap.put("province", lives.get("province"));
                    resultMap.put("city", lives.get("city"));
                    resultMap.put("weather", lives.get("weather"));
                    resultMap.put("temperature", lives.get("temperature"));
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

        //TODO 获取状态
        resultMap.put("status", "出差中");
        return resultMap;
    }

    /**
     * 编程语言工时数据
     *
     * @return 结果集
     * @date 2022/11/17 15:48
     * @author ☪wl
     */
    public Map<String, Object> findWorkLanguageData() {
        Map<String, Object> resultMap = new HashMap<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("wakatime.com")
                    .setPath("/api/v1/users/current/stats/last_7_days")
                    .setParameter("api_key", this.keyValueOptionsService.getById("WakaTimeKey").getOptValue())
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    //语言列表
                    JSONArray languages = jsonData.getJSONArray("languages");
                    List<Map<String, Object>> languageMapList = new ArrayList<>();
                    //计算总时间
                    double countTime = 0;
                    //只拿6个
                    for (int i = 0; i < languages.size() && i < 6; i++) {
                        Map<String, Object> languagesMap = new HashMap<>(2);
                        JSONObject languagesJsonObject = languages.getJSONObject(i);
                        languagesMap.put("name", languagesJsonObject.get("name"));
                        languagesMap.put("timeText", languagesJsonObject.get("text"));
                        countTime += languagesJsonObject.getDouble("total_seconds");
                        languagesMap.put("time", languagesJsonObject.get("total_seconds"));
                        languageMapList.add(languagesMap);
                    }
                    resultMap.put("languages", languageMapList);
                    //总时间
                    resultMap.put("languagesCountTime", countTime);
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

        return resultMap;
    }

    public Map<String, Object> findWorkTimeData() {
        Map<String, Object> resultMap = new HashMap<>();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //昨天的日期
            Calendar yesterdayCal = Calendar.getInstance();
            yesterdayCal.add(Calendar.DATE, -1);

            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("wakatime.com")
                    .setPath("/api/v1/users/current/summaries")
                    .setParameter("start", sdf.format(yesterdayCal.getTime()))
                    .setParameter("end", sdf.format(new Date()))
                    .setParameter("api_key", this.keyValueOptionsService.getById("WakaTimeKey").getOptValue())
                    .build();
            HttpGet httpGet = new HttpGet(uri);
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(entity));
                    JSONArray jsonDataArray = jsonObject.getJSONArray("data");
                    resultMap.put("yesterdayWorkTime", jsonDataArray.getJSONObject(0).getJSONObject("grand_total").get("text"));
                    resultMap.put("todayWorkTime", jsonDataArray.getJSONObject(1).getJSONObject("grand_total").get("text"));
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

        return resultMap;
    }
}
