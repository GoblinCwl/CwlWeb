package com.goblincwl.cwlweb.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.EmailUtil;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabsSubscribe;
import com.goblincwl.cwlweb.modules.blog.service.BlogTabsService;
import com.goblincwl.cwlweb.modules.blog.service.BlogTabsSubscribeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 文章标签订阅 Controller
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/09 15:05
 */
@RestController
@RequestMapping(BlogController.MODULE_PREFIX + "/blogTabsSubscribe")
@RequiredArgsConstructor
public class BlogTabsSubscribeController extends BaseController<BlogTabsSubscribe> {

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private final BlogTabsSubscribeService blogTabsSubscribeService;

    /**
     * 发送邮件验证码
     *
     * @param email 邮箱
     * @return 反馈
     * @date 2022/12/9 14:01
     * @author ☪wl
     */
    @GetMapping("/sendVerificationEmail")
    public Result<Object> sendVerificationEmail(String email) throws Exception {
        if (StringUtils.isNotEmpty(email)) {
            String verificationCode = creatCode(5);
            String redisKey = "verificationCode-" + email;
            //从Redis中获取，如果存在，不予发送
            Object oldValue = this.redisTemplate.opsForValue().get(redisKey);
            if (oldValue != null) {
                throw new GoblinCwlException("同邮箱请等待至少5分钟后发送！");
            }

            //存到redis
            this.redisTemplate.opsForValue().set(redisKey, verificationCode);
            //5分钟有效
            //设置有效期
            redisTemplate.expire(redisKey, 5, TimeUnit.MINUTES);
            EmailUtil.sendMail(email, email, "Cwl-Web 用于订阅操作的验证码", "您的验证码是：" + verificationCode + "<br>验证码5分钟内有效。");
            return Result.genSuccess("发送成功");
        }
        return Result.genFail("发送失败");
    }

    /**
     * 生成随机验证码
     *
     * @param n 位数
     * @return 验证码
     * @date 2022/12/9 14:04
     * @author ☪wl
     */
    private String creatCode(int n) {
        n = n - 1;
        //定义一个字符串变量 记录生成的随机数
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        //2.在方法内部使用for循环生成指定位数的随机字符，并连接起来
        for (int i = 0; i <= n; i++) {
            //生成一个随机字符：大写 ，小写 ，数字（0  1  2）
            int type = r.nextInt(2);
            switch (type) {
                //大写字母  65   ~   65+25
                case 0:
                    char ch = (char) (r.nextInt(26) + 65);
                    code.append(ch);
                    break;
                //大写字母  65   ~   65+25
                case 1:
                    code.append(r.nextInt(10));
                    break;
                default:
                    break;
            }
        }
        return code.toString();
    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 反馈
     * @date 2022/12/9 14:16
     * @author ☪wl
     */
    @PutMapping("/checkVerificationCode")
    public Result<Object> checkVerificationCode(String email, String code) {
        String redisKey = "verificationCode-" + email;
        Object redisObj = this.redisTemplate.opsForValue().get(redisKey);
        if (redisObj != null && redisObj.equals(code)) {
            //删除验证码redis
            this.redisTemplate.delete(redisKey);
            //生成一个随机的UUID用于身份验证
            String uuid = UUID.randomUUID().toString();
            //存入redis用于身份验证
            String uuidRedisKey = "verificationUUID-" + email;
            this.redisTemplate.opsForValue().set(uuidRedisKey, uuid);
            //两小时有效
            this.redisTemplate.expire(uuidRedisKey, 2, TimeUnit.HOURS);
            return new Result<>().success(uuid, "验证成功");
        }
        return Result.genSuccess("验证码错误");
    }

    /**
     * 提交订阅操作
     *
     * @param email       邮箱
     * @param tabsId      标签ID
     * @param isSubscribe 是否是订阅
     * @return 反馈
     * @date 2022/12/9 15:37
     * @author ☪wl
     */
    @PutMapping("/submitSubscribe")
    public Result<Object> submitSubscribe(String email, Integer tabsId, Boolean isSubscribe, String uuid) {
        //验证UUID
        Object redisObj = this.redisTemplate.opsForValue().get("verificationUUID-" + email);
        if (redisObj != null && redisObj.equals(uuid)) {
            if (isSubscribe) {
                BlogTabsSubscribe blogTabsSubscribe = new BlogTabsSubscribe();
                blogTabsSubscribe.setBlogTabsId(tabsId);
                blogTabsSubscribe.setEmail(email);
                this.blogTabsSubscribeService.save(blogTabsSubscribe);
            } else {
                this.blogTabsSubscribeService.remove(
                        new LambdaQueryWrapper<BlogTabsSubscribe>()
                                .eq(BlogTabsSubscribe::getEmail, email)
                                .eq(BlogTabsSubscribe::getBlogTabsId, tabsId)
                );
            }
        } else {
            throw new GoblinCwlException("邮箱未验证");
        }
        return Result.genSuccess(isSubscribe ? "订阅" : "取消订阅" + "成功");
    }

}
