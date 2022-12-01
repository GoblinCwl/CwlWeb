package com.goblincwl.cwlweb.common.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.goblincwl.cwlweb.blog.entity.Blog;
import com.goblincwl.cwlweb.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author ☪wl
 */
@Component
@RequiredArgsConstructor
public class BrowserTimesSchedule {

    @Resource(name = "redisStringTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final BlogService blogService;

    @Scheduled(cron = "0 0 * ? * *")
    private void process() {
        Set<String> browserTimesRedisKeySet = redisTemplate.keys("blogBrowserTimes*");
        if (!CollectionUtils.isEmpty(browserTimesRedisKeySet)) {
            for (String key : browserTimesRedisKeySet) {
                Integer id = Integer.parseInt(key.substring("blogBrowserTimes".length()));
                Blog blog = blogService.getById(id);
                Long redisBrowserTimes = (Long) this.redisTemplate.opsForValue().get(key);
                if (redisBrowserTimes != null) {
                    if (blog.getBrowserTimes() < redisBrowserTimes) {
                        blogService.update(
                                new LambdaUpdateWrapper<Blog>().eq(Blog::getId, id).set(Blog::getBrowserTimes, redisBrowserTimes)
                        );
                    }
                }
            }
        }
    }
}

