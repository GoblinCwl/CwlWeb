package com.goblincwl.cwlweb.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.blog.entity.Comment;
import com.goblincwl.cwlweb.blog.service.CommentService;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.manager.entity.AccessRecord;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 评论 Controller
 *
 * @author ☪wl
 * @date 2021-05-10 15:38
 */
@RestController
@RequestMapping(BlogController.MODULE_PREFIX + "/comment")
@RequiredArgsConstructor
public class CommentController extends BaseController<Comment> {

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    private final CommentService commentService;
    private final AccessRecordService accessRecordService;

    /**
     * 分页主查询
     *
     * @param comment 查询参数
     * @return 结果集
     * @date 2021-05-10 15:40:06
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<Comment>> list(Comment comment) {
        //仅查询1级评论
        QueryWrapper<Comment> queryWrapper = createQueryWrapper(comment);
        queryWrapper.isNull("parent_id");
        Page<Comment> page = this.commentService.page(createPage(), queryWrapper);
        //查询子评论
        for (Comment parentComment : page.getRecords()) {
            QueryWrapper<Comment> childrenQueryMapper = new QueryWrapper<>();
            childrenQueryMapper.eq("parent_id", parentComment.getId());
            parentComment.setChildrenList(this.commentService.list(childrenQueryMapper));
        }
        return new Result<Page<Comment>>().success(page, "成功");
    }

    /**
     * 不分页主查询
     *
     * @param comment 查询参数
     * @return 结果集
     * @date 2021-05-10 15:44:28
     * @author ☪wl
     */
    @GetMapping("/listAll")
    public Result<List<Comment>> listAll(Comment comment) {
        //仅查询1级评论
        QueryWrapper<Comment> queryWrapper = createQueryWrapper(comment);
        queryWrapper.isNull("parent_id");
        List<Comment> list = this.commentService.list(queryWrapper);
        //查询子评论
        for (Comment parentComment : list) {
            QueryWrapper<Comment> childrenQueryMapper = new QueryWrapper<>();
            childrenQueryMapper.eq("parent_id", parentComment.getId());
            parentComment.setChildrenList(this.commentService.list(childrenQueryMapper));
        }
        return new Result<List<Comment>>().success(list, "成功");
    }


    /**
     * 新增
     *
     * @param comment 数据参数
     * @return 反馈
     * @date 2021-05-10 16:11:19
     * @author ☪wl
     */
    @PostMapping("/add")
    public Result<Object> add(Comment comment) {
        //保存评论
        this.commentService.save(comment);
        //更新用户信息
        AccessRecord accessRecord = this.accessRecordService.getOne(
                new LambdaQueryWrapper<AccessRecord>()
                        .eq(AccessRecord::getIpAddress, comment.getIpAddress())
        );
        if (accessRecord != null) {
            accessRecord.setNickName(comment.getNickName());
            //更新访问记录
            this.accessRecordService.updateById(accessRecord);
            //更新Redis缓存
            redisTemplate.opsForValue().set("ipAccessCache:" + accessRecord.getIpAddress(), accessRecord);
            //设置有效期为30min
            redisTemplate.expire("ipAccessCache:" + accessRecord.getIpAddress(), 30, TimeUnit.MINUTES);
        }
        //TODO 回复发邮件
        return Result.genSuccess("评论成功");
    }

    /**
     * 删除
     *
     * @param ids 主键（逗号拼接）
     * @return 反馈
     * @date 2021-05-10 16:10:30
     * @author ☪wl
     */
    @TokenCheck
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            this.commentService.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.genSuccess("删除成功");
    }

}
