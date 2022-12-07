package com.goblincwl.cwlweb.common.aspect;

import com.goblincwl.cwlweb.modules.app.entitiy.App;
import com.goblincwl.cwlweb.modules.app.service.AppService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author ☪wl
 */
@Aspect
@Component
@Order(2)
@RequiredArgsConstructor
public class UsesTimeAspect {
    @Resource(name = "redisStringTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final AppService appService;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.goblincwl.cwlweb.common.annotation.aop.UsesTimes)")
    public void checkPoint() {
    }

    /**
     * 环绕获取请求参数
     *
     * @param joinPoint joinPoint
     */
    @After("checkPoint()")
    public void afterMethod(JoinPoint joinPoint) {
        //获取方法的参数
        Object[] args = joinPoint.getArgs();
        //应用ID
        String id = (String) args[0];
        this.recordUsesTimesData(id);
    }

    /**
     * 对redis进行操作
     *
     * @param appId 应用ID
     * @date 2022/12/1 17:36
     * @author ☪wl
     */
    private synchronized void recordUsesTimesData(String appId) {
        //redisKey
        String redisKey = "appUsesTimes" + appId;
        String usesTimes = String.valueOf(this.redisTemplate.opsForValue().get(redisKey));
        if (StringUtils.isEmpty(usesTimes) || "null".equals(usesTimes)) {
            //获取数据库中的浏览次数
            App app = this.appService.getById(appId);
            this.redisTemplate.opsForValue().setIfAbsent(redisKey, app.getUsesTimes() + 1);
        } else {
            this.redisTemplate.opsForValue().setIfPresent(redisKey, Long.parseLong(usesTimes) + 1);
        }
        //1天有效期
        redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
    }
}
