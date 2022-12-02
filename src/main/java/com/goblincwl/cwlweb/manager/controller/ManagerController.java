package com.goblincwl.cwlweb.manager.controller;

import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.goblincwl.cwlweb.blog.entity.Blog;
import com.goblincwl.cwlweb.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.blog.service.BlogService;
import com.goblincwl.cwlweb.blog.service.BlogTabsService;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.handler.IndexTerminalWebSocketHandler;
import com.goblincwl.cwlweb.common.utils.IpUtils;
import com.goblincwl.cwlweb.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.manager.service.AccessLogService;
import com.goblincwl.cwlweb.manager.service.KeyValueOptionsService;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import java.util.stream.Collectors;

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
    private final BlogService blogService;
    private final BlogTabsService blogTabsService;

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
            //访问量
            Map<String, Object> accessMap = new HashMap<>(2);
            //总访问量
            Long allAccessCount = this.accessLogService.count();
            accessMap.put("all", allAccessCount);
            //今日访问量
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String todayDateFormat = sdf.format(new Date());
            Long todayAccessCount = this.accessLogService.countByDate(todayDateFormat);
            accessMap.put("today", todayAccessCount);
            resultMap.put("accessCount", accessMap);
            //TODO 昨日订阅量
            //TODO 今日订阅量
            //7天访问数据
            Long[] accessDataArray = new Long[7];
            String[] dateArray = new String[7];
            SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd");
            //-- 今天
            accessDataArray[6] = todayAccessCount;
            dateArray[6] = sdfDate.format(new Date());
            //昨天起往前6天
            Calendar cal = Calendar.getInstance();
            for (int i = 5; i >= 0; i--) {
                cal.add(Calendar.DATE, -1);
                String nowFormat = sdf.format(cal.getTime());
                Long nowCount = this.accessLogService.countByDate(nowFormat);
                accessDataArray[i] = nowCount;
                dateArray[i] = sdfDate.format(cal.getTime());
            }
            resultMap.put("accessDataArray", accessDataArray);
            //统计日期字符串
            resultMap.put("dateArray", dateArray);
            //TODO 7天订阅数据
            Long[] subscribeDataArray = new Long[7];
            subscribeDataArray[0] = 0L;
            subscribeDataArray[1] = 0L;
            subscribeDataArray[2] = 0L;
            subscribeDataArray[3] = 0L;
            subscribeDataArray[4] = 0L;
            subscribeDataArray[5] = 0L;
            subscribeDataArray[6] = 0L;
            resultMap.put("subscribeDataArray", subscribeDataArray);
            //最热门5篇文章
            List<Blog> hotBlogList = this.blogService.list(
                    new LambdaUpdateWrapper<Blog>()
                            .orderBy(true, false, Blog::getBrowserTimes)
                            .last("limit 5")
            );
            resultMap.put("hotBlogList", hotBlogList);
            //最热门5个标签
            List<BlogTabs> hotBlogTabsList = this.blogTabsService.list(
                    new LambdaUpdateWrapper<BlogTabs>()
                            .orderBy(true, false, BlogTabs::getSubscribeCount)
                            .last("limit 5")
            );
            resultMap.put("hotBlogTabsList", hotBlogTabsList);
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
        MetricsEndpoint.MetricResponse jvmMaxMemoryMetric = this.metricsEndpoint.metric("jvm.memory.max", null);
        Map<String, Object> jvmMemoryMap = new HashMap<>(2);
        jvmMemoryMap.put("committed", jvmCommittedMemoryMetric.getMeasurements().get(0).getValue());
        jvmMemoryMap.put("used", jvmUsedMemoryMetric.getMeasurements().get(0).getValue());
        jvmMemoryMap.put("max", jvmMaxMemoryMetric.getMeasurements().get(0).getValue());
        resultMap.put("jvmMemory", jvmMemoryMap);
        //CPU占用
        MetricsEndpoint.MetricResponse cpuUsageMetric = this.metricsEndpoint.metric("process.cpu.usage", null);
        resultMap.put("cpuUsage", cpuUsageMetric.getMeasurements().get(0).getValue());
        //HTTP请求统计
        WebAppStatManager webAppStatManager = WebAppStatManager.getInstance();
        List<Map<String, Object>> uriList = webAppStatManager.getURIStatData().stream().filter(data -> {
            String uri = String.valueOf(data.get("URI"));
            return !uri.contains("webjars")
                    && !uri.contains("images")
                    && !uri.contains("fonts")
                    && !"/druid".equals(uri)
                    && !"/favicon.ico".equals(uri)
                    && !"/log".equals(uri)
                    && !"/terminal".equals(uri);
        }).sorted((o1, o2) -> Long.compare((Long) o2.get("RequestCount"), (Long) o1.get("RequestCount"))).collect(Collectors.toList());
        resultMap.put("uriList", uriList);
        //聊天室在线
        resultMap.put("socketOnlineCount", IndexTerminalWebSocketHandler.getOnlineCount());

        return Result.genSuccess(resultMap, "成功");
    }
}
