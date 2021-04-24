package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.index.entity.AccessRecord;
import com.goblincwl.cwlweb.index.service.AccessRecordService;
import com.goblincwl.cwlweb.utils.IpUtils;
import com.goblincwl.cwlweb.utils.NickNameUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 重定向 Controller
 *
 * @author ☪wl
 * @date 2021-04-23 16:12
 */
@Controller
@AllArgsConstructor
@RequestMapping("/")
public class RedirectController {

    @Resource(name = "customRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    private final AccessRecordService accessRecordService;

    /**
     * 首页
     */
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index/index");

        String ipAddress = IpUtils.getIpAddress(request);
        AccessRecord accessRecord;
        //首先查询Redis中是否记录
        accessRecord = (AccessRecord) redisTemplate.opsForValue().get("ipAccessCache:" + ipAddress);
        if (accessRecord == null) {
            //为空，则已超过时间，操作数据库
            accessRecord = new AccessRecord();
            accessRecord.setIpAddress(ipAddress);

            AccessRecord accessRecordResult = this.accessRecordService.findOne(accessRecord);
            if (accessRecordResult == null) {
                //保存访客信息
                accessRecord.setNickName(NickNameUtils.randomName(2));
                accessRecord.setAccessTime(new Date());
                Integer saveOneResult = this.accessRecordService.saveOne(accessRecord);
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
                Integer updateOneResult = this.accessRecordService.updateOne(accessRecord);
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
        modelAndView.addObject("accessTime", sdf.format(accessRecord.getAccessTime()));
        //上次访问
        modelAndView.addObject("lastAccessTime", accessRecord.getLastAccessTime() == null ? "首次访问" : sdf.format(accessRecord.getLastAccessTime()));
        //总访问次数
        modelAndView.addObject("accessCount", this.accessRecordService.findAccessCount());
        //总访问IP数
        modelAndView.addObject("accessIpCount", accessRecord.getId() == null ? this.accessRecordService.findAccessIpCount() : accessRecord.getId());
        //昵称
        modelAndView.addObject("nickName", accessRecord.getNickName());
        return modelAndView;
    }

    /**
     * 简历
     */
    @GetMapping("/resume")
    public String resume() {
        return "resume/resume";
    }

}
