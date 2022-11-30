package com.goblincwl.cwlweb.manager.controller;

import com.alibaba.fastjson.JSONObject;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.manager.service.AccessLogService;
import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    private final MetricsEndpoint metricsEndpoint;
    private final HealthEndpoint healthEndpoint;
    private final AccessLogService accessLogService;

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

    /**
     * Token校验
     *
     * @param request 请求对象
     * @return 结果
     * @date 2021-05-12 17:30:44
     * @author ☪wl
     */
    @GetMapping("/check")
    public Result<Object> check(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return Result.genSuccess(this.tokenService.checkToken(token, IpUtils.getIpAddress(request)), "成功");
    }

    /**
     * 管理看板左侧数据
     *
     * @return 结果
     * @date 2022/11/30 9:40
     * @author ☪wl
     */
    @GetMapping("/managerLeft")
    public Result<Object> managerLeft() {
        Map<String, Object> resultMap;
        //先从Redis拿缓存
        String redisKey = "managerLeftData";
        String managerLeftData = (String) redisTemplate.opsForValue().get(redisKey);
        boolean isRedis = !StringUtils.isEmpty(managerLeftData);
        if (!isRedis) {
            //从数据库查询数据
            resultMap = new HashMap<>();
            //昨日访问量
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date yesterday = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String yesterdayDateFormat = sdf.format(yesterday);
            Long yesterdayAccessCount = this.accessLogService.countByDate(yesterdayDateFormat);
            resultMap.put("yesterdayAccessCount", yesterdayAccessCount);
            //今日访问量
            String todayDateFormat = sdf.format(new Date());
            Long todayAccessCount = this.accessLogService.countByDate(todayDateFormat);
            resultMap.put("todayAccessCount", todayAccessCount);
            //TODO 昨日订阅量
            //TODO 今日订阅量
            //7天访问数据
            List<Map<String, Object>> weekData = new ArrayList<>();
            //-- 昨天
            Map<String, Object> yesterdayMap = new HashMap<>(2);
            yesterdayMap.put("date", yesterdayDateFormat);
            yesterdayMap.put("count", yesterdayAccessCount);
            weekData.add(yesterdayMap);
            //-- 今天
            Map<String, Object> todayMap = new HashMap<>(2);
            todayMap.put("date", todayDateFormat);
            todayMap.put("count", yesterdayAccessCount);
            weekData.add(todayMap);
            //昨天起往前5天
            for (int i = 0; i < 5; i++) {
                cal.add(Calendar.DATE, -1);
                String nowFormat = sdf.format(cal.getTime());
                Long nowCount = this.accessLogService.countByDate(nowFormat);
                Map<String, Object> nowMap = new HashMap<>(2);
                nowMap.put("date", nowFormat);
                nowMap.put("count", nowCount);
                weekData.add(nowMap);
            }
            resultMap.put("accessWeekList", weekData);
            //TODO 7天订阅数据
            //TODO 最热门5篇文章
            //TODO 最热门5个标签
            //TODO 最热门5个功能


            //存到Redis
            redisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(resultMap));
            //设置有效期
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
        } else {
            resultMap = JSONObject.parseObject(managerLeftData, Map.class);
        }
        return Result.genSuccess(resultMap, "成功");
    }

    /**
     * 管理看板右侧数据(高刷)
     *
     * @return 数据
     * @date 2022/11/30 10:04
     * @author ☪wl
     */
    @GetMapping("/managerRight")
    public Result<Object> managerRight() {
        Map<String, Object> resultMap = new HashMap<>();
        //服务器运行时间
        MetricsEndpoint.MetricResponse serverUptimeMetric = this.metricsEndpoint.metric("process.uptime", null);
        resultMap.put("serverUptime", serverUptimeMetric.getMeasurements().get(0).getValue());
        //GC时间
        MetricsEndpoint.MetricResponse gcTimeMetric = this.metricsEndpoint.metric("jvm.gc.pause", null);
        List<MetricsEndpoint.Sample> gcTimeMetricMeasurements = gcTimeMetric.getMeasurements();
        Map<String, Object> gcMap = new HashMap<>(2);
        gcMap.put("count", gcTimeMetricMeasurements.get(0).getValue());
        gcMap.put("time", gcTimeMetricMeasurements.get(1).getValue());
        resultMap.put("gcTime", gcMap);
        //JVM守护线程
        Map<String, Object> jvmThreadMap = new HashMap<>(3);
        MetricsEndpoint.MetricResponse jvmGuardThreadMetric = this.metricsEndpoint.metric("jvm.threads.daemon", null);
        jvmThreadMap.put("daemonCount", jvmGuardThreadMetric.getMeasurements().get(0).getValue());
        //JVM活跃线程
        MetricsEndpoint.MetricResponse jvmLiveThreadMetric = this.metricsEndpoint.metric("jvm.threads.live", null);
        jvmThreadMap.put("liveCount", jvmLiveThreadMetric.getMeasurements().get(0).getValue());
        //JVM峰值线程
        MetricsEndpoint.MetricResponse jvmPeakThreadMetric = this.metricsEndpoint.metric("jvm.threads.peak", null);
        jvmThreadMap.put("peakCount", jvmPeakThreadMetric.getMeasurements().get(0).getValue());
        resultMap.put("jvmThread", jvmThreadMap);
        //磁盘空间
        SystemHealth health = (SystemHealth) this.healthEndpoint.health();
        resultMap.put("diskSpace", (health.getComponents().get("diskSpace")));
        //内存占用
        MetricsEndpoint.MetricResponse jvmCommittedMemoryMetric = this.metricsEndpoint.metric("jvm.memory.committed", null);
        MetricsEndpoint.MetricResponse jvmUsedMemoryMetric = this.metricsEndpoint.metric("jvm.memory.used", null);
        Map<String, Object> jvmMemoryMap = new HashMap<>(2);
        jvmMemoryMap.put("committed", jvmCommittedMemoryMetric.getMeasurements().get(0).getValue());
        jvmMemoryMap.put("used", jvmUsedMemoryMetric.getMeasurements().get(0).getValue());
        resultMap.put("jvmMemory", jvmMemoryMap);
        //CPU占用
        MetricsEndpoint.MetricResponse cpuUsageMetric = this.metricsEndpoint.metric("process.cpu.usage", null);
        resultMap.put("cpuUsage", cpuUsageMetric.getMeasurements().get(0).getValue());
        //HTTP请求统计

        return Result.genSuccess(resultMap, "成功");
    }
}
