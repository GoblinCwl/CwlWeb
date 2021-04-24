package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.index.entity.AccessRecord;
import com.goblincwl.cwlweb.index.service.AccessRecordService;
import com.goblincwl.cwlweb.utils.IpUtils;
import com.goblincwl.cwlweb.utils.NickNameUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    private final AccessRecordService accessRecordService;

    /**
     * 首页
     */
    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index/index");

        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setIpAddress(IpUtils.getIpAddress(request));

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //本次访问
        modelAndView.addObject("accessTime", sdf.format(accessRecord.getAccessTime()));
        //上次访问
        modelAndView.addObject("lastAccessTime", accessRecord.getLastAccessTime() == null ? "首次访问" : sdf.format(accessRecord.getLastAccessTime()));
        //总访问次数
        modelAndView.addObject("accessCount", this.accessRecordService.findAccessCount());
        //总访问IP数
        modelAndView.addObject("accessIpCount", accessRecord.getId());
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
