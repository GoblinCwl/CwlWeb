package com.goblincwl.cwlweb.index.service;

import com.goblincwl.cwlweb.common.exception.GoblinCwlException;
import com.goblincwl.cwlweb.common.utils.NickNameUtils;
import com.goblincwl.cwlweb.index.entity.AccessRecord;
import com.goblincwl.cwlweb.index.mapper.AccessRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 访问记录 Service
 *
 * @author ☪wl
 * @date 2021-04-24 18:34
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AccessRecordService {

    @Resource(name = "customRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    private final AccessRecordMapper accessRecordMapper;

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
        AccessRecord accessRecord;
        //首先查询Redis中是否记录
        accessRecord = (AccessRecord) redisTemplate.opsForValue().get("ipAccessCache:" + ipAddress);
        if (accessRecord == null) {
            //为空，则已超过时间，操作数据库
            accessRecord = new AccessRecord();
            accessRecord.setIpAddress(ipAddress);

            AccessRecord accessRecordResult = this.accessRecordMapper.selectOne(accessRecord);
            if (accessRecordResult == null) {
                //保存访客信息
                accessRecord.setNickName(NickNameUtils.randomName(2));
                accessRecord.setAccessTime(new Date());
                Integer saveOneResult = this.accessRecordMapper.insertOne(accessRecord);
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
                Integer updateOneResult = this.accessRecordMapper.updateOne(accessRecord);
                if (updateOneResult < 1) {
                    //TODO
                    throw new RuntimeException("修改失败");
                }
            }
            //新增Redis缓存
            redisTemplate.opsForValue().set("ipAccessCache:" + ipAddress, accessRecord);
            //设置有效期为30min
            redisTemplate.expire("ipAccessCache:" + ipAddress, 30, TimeUnit.MINUTES);
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
        return resultMap;
    }
}
