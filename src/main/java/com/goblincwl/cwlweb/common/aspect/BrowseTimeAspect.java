package com.goblincwl.cwlweb.common.aspect;

import com.goblincwl.cwlweb.modules.blog.entity.Blog;
import com.goblincwl.cwlweb.modules.blog.service.BlogService;
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
public class BrowseTimeAspect {
    @Resource(name = "redisStringTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final BlogService blogService;

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.goblincwl.cwlweb.common.annotation.aop.BrowseTimes)")
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
        //文章ID
        String id = (String) args[0];
        this.recordBrowserTimesData(id);
    }

    /**
     * 对redis进行操作
     *
     * @param blogId 文章ID
     * @date 2022/12/1 17:36
     * @author ☪wl
     */
    private synchronized void recordBrowserTimesData(String blogId) {
        //redisKey
        String redisKey = "blogBrowserTimes" + blogId;
        String browserTimes = String.valueOf(this.redisTemplate.opsForValue().get(redisKey));
        if (StringUtils.isEmpty(browserTimes) || "null".equals(browserTimes)) {
            //获取数据库中的浏览次数
            Blog blog = this.blogService.getById(blogId);
            if (blog != null) {
                this.redisTemplate.opsForValue().setIfAbsent(redisKey, blog.getBrowserTimes() + 1);
            }
        } else {
            this.redisTemplate.opsForValue().setIfPresent(redisKey, Long.parseLong(browserTimes) + 1);
        }
        //1天有效期
        redisTemplate.expire(redisKey, 1, TimeUnit.DAYS);
    }
}
