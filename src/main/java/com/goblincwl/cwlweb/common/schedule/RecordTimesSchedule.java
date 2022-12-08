package com.goblincwl.cwlweb.common.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.goblincwl.cwlweb.modules.app.entitiy.App;
import com.goblincwl.cwlweb.modules.app.service.AppService;
import com.goblincwl.cwlweb.modules.blog.entity.Blog;
import com.goblincwl.cwlweb.modules.blog.service.BlogService;
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
public class RecordTimesSchedule {

    @Resource(name = "redisStringTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final BlogService blogService;

    private final AppService appService;

    @Scheduled(cron = "0 0 * ? * *")
    private void process() {
        Set<String> browserTimesRedisKeySet = redisTemplate.keys("blogBrowserTimes*");
        if (!CollectionUtils.isEmpty(browserTimesRedisKeySet)) {
            for (String key : browserTimesRedisKeySet) {
                Integer id = Integer.parseInt(key.substring("blogBrowserTimes".length()));
                Blog blog = blogService.getById(id);
                Integer redisValue = (Integer) this.redisTemplate.opsForValue().get(key);
                Long redisBrowserTimes = redisValue == null ? 0 : redisValue.longValue();
                if (blog == null) {
                    this.redisTemplate.delete(key);
                }else {
                    if (blog.getBrowserTimes() < redisBrowserTimes) {
                        blogService.update(
                                new LambdaUpdateWrapper<Blog>().eq(Blog::getId, id).set(Blog::getBrowserTimes, redisBrowserTimes)
                        );
                    }
                }
            }
        }
        Set<String> appUsesTimesRedisKeySet = redisTemplate.keys("appUsesTimes*");
        if (!CollectionUtils.isEmpty(appUsesTimesRedisKeySet)) {
            for (String key : appUsesTimesRedisKeySet) {
                Integer id = Integer.parseInt(key.substring("appUsesTimes".length()));
                App app = appService.getById(id);
                Integer redisValue = (Integer) this.redisTemplate.opsForValue().get(key);
                Long redisUsesTimes = redisValue == null ? 0 : redisValue.longValue();
                if (app == null){
                    this.redisTemplate.delete(key);
                }else {
                    if (app.getUsesTimes() < redisUsesTimes) {
                        appService.update(
                                new LambdaUpdateWrapper<App>().eq(App::getId, id).set(App::getUsesTimes, redisUsesTimes)
                        );
                    }
                }
            }
        }
    }
}

