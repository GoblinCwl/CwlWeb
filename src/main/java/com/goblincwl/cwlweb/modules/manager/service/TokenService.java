package com.goblincwl.cwlweb.modules.manager.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token认证 Service
 *
 * @author ☪wl
 * @date 2021-05-03 14:50
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TokenService {

    /**
     * 默认Token有效期：5小时
     */
    private final static long EXPIRE_TIME = 300 * 60;
    /**
     * redis中Token的key前缀
     */
    private final static String KEY_PREFIX = "managerAccessToken:";

    @Resource(name = "redisStringTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 使用UUID,生成Token
     * 将IP作为Value存入redis
     *
     * @param ipAddr ip地址
     * @return token
     * @date 2021-05-03 14:56:03
     * @author ☪wl
     */
    public String genToken(String ipAddr) {
        return genToken(ipAddr, EXPIRE_TIME);
    }

    /**
     * 使用UUID,生成Token
     * 将IP作为Value存入redis
     * 指定有效期
     *
     * @param ipAddr     ip地址
     * @param expireTime 有效期(秒)
     * @return token
     * @date 2021-05-03 14:56:03
     * @author ☪wl
     */
    public String genToken(String ipAddr, Long expireTime) {
        //删除旧Token
        Set<String> keys = this.redisTemplate.keys(KEY_PREFIX + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            this.redisTemplate.delete(keys);
        }
        String token = UUID.randomUUID().toString();
        this.redisTemplate.opsForValue().set(
                KEY_PREFIX + token,
                ipAddr,
                expireTime == null ? EXPIRE_TIME : expireTime,
                TimeUnit.SECONDS);
        return token;
    }

    /**
     * 验证Token与IP
     *
     * @param ipAddr ip地址
     * @return 验证结果
     * @date 2021-05-03 14:58:48
     * @author ☪wl
     */
    public Boolean checkToken(String token, String ipAddr) {
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(ipAddr)) {
            return false;
        }
        String redisIpAddr = (String) this.redisTemplate.opsForValue().get(KEY_PREFIX + token);
        return ipAddr.equals(redisIpAddr);
    }

    /**
     * 清除Token
     *
     * @param token token
     * @date 2021-05-04 23:33:32
     * @author ☪wl
     */
    public void clearToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            this.redisTemplate.delete(KEY_PREFIX + token);
        }
    }

}