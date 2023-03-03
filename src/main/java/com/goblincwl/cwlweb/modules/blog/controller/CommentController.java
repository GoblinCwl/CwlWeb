package com.goblincwl.cwlweb.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
import com.goblincwl.cwlweb.modules.blog.service.CommentService;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * 评论 Controller
 *
 * @author ☪wl
 * @date 2021-05-10 15:38
 */
@RestController
@RequestMapping(BlogController.MODULE_PREFIX + "/comment")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CommentController extends BaseController<Comment> {

    private final CommentService commentService;
    private final KeyValueOptionsService keyValueOptionsService;


    /**
     * 分页主查询 - 用于管理
     *
     * @param comment 查询参数
     * @return 结果集
     * @date 2021-05-10 15:40:06
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<Comment>> list(Comment comment) {
        MPJQueryWrapper<Comment> mpjQueryWrapper = new MPJQueryWrapper<>();
        mpjQueryWrapper.selectAll(Comment.class);
        //查询参数
        if (StringUtils.isNotEmpty(comment.getContent())) {
            mpjQueryWrapper.like("t.content", comment.getContent());
        }
        mpjQueryWrapper.isNull("t.parent_id");
        //排序分页
        String sortName = ServletUtils.getParameter("sortName");
        String sortOrder = ServletUtils.getParameter("sortOrder");
        if (sortName != null && sortOrder != null) {
            mpjQueryWrapper.orderBy(true, "asc".equals(sortOrder), sortName);
        }
        //连表
        mpjQueryWrapper.leftJoin("blog t1 on t1.id = t.blog_id");
        mpjQueryWrapper.select("t1.title as `blog.title`");

        //连表子评论
        mpjQueryWrapper.leftJoin("comment t2 on t2.parent_id = t.id");
        //审核状态查询
        if (StringUtils.isNotEmpty(comment.getWebsiteAuditStr())) {
            mpjQueryWrapper.and(queryWrapper -> {
                for (String websiteAudit : comment.getWebsiteAuditStr().split(",")) {
                    queryWrapper.or(andWrapper -> andWrapper.eq("t.website_audit", websiteAudit).or().eq("t2.website_audit", websiteAudit));
                    queryWrapper.isNotNull("t.website").ne("t.website", "");
                }
            });
        }

        //分组
        mpjQueryWrapper.groupBy("t.id", " t.parent_id", " t.blog_id", " t.nick_name", " t.profile_url", " t.content", " t.send_time", " t.email", " t.website");
        Page<Comment> page = this.commentService.page(createPage(), mpjQueryWrapper);
        //查询子评论
        for (Comment parentComment : page.getRecords()) {
            QueryWrapper<Comment> childrenQueryMapper = new QueryWrapper<>();
            childrenQueryMapper.eq("parent_id", parentComment.getId());
            //审核状态查询
            if (StringUtils.isNotEmpty(comment.getWebsiteAuditStr())) {
                childrenQueryMapper.and(queryWrapper -> {
                    for (String websiteAudit : comment.getWebsiteAuditStr().split(",")) {
                        queryWrapper.or().eq("website_audit", websiteAudit);
                        queryWrapper.isNotNull("website").ne("website", "");
                    }
                });
            }
            parentComment.setChildrenList(this.commentService.list(childrenQueryMapper));
        }

        return new Result<Page<Comment>>().success(page, "成功");
    }

    /**
     * 不分页主查询 - 用于页面展示
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
            //子评论
            QueryWrapper<Comment> childrenQueryMapper = new QueryWrapper<>();
            childrenQueryMapper.eq("parent_id", parentComment.getId());
            List<Comment> childrenList = this.commentService.list(childrenQueryMapper);
            //屏蔽未审核的网址
            if (parentComment.getWebsiteAudit() == 0) {
                parentComment.setWebsite("");
            }
            childrenList.forEach(childrenComment -> {
                if (childrenComment.getWebsiteAudit() == 0) {
                    childrenComment.setWebsite("");
                }
            });
            parentComment.setChildrenList(childrenList);
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
    public Result<Object> add(HttpServletRequest request, Comment comment) throws GeneralSecurityException, IOException {
        Integer commentId = this.commentService.add(request, comment);
        return new Result<>().success(commentId, "评论成功");
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
            List<String> idList = Arrays.asList(ids.split(","));
            //删除评论
            this.commentService.removeByIds(idList);
            //删除子评论
            this.commentService.remove(new LambdaQueryWrapper<Comment>().in(Comment::getParentId, idList));
        }
        return Result.genSuccess("删除成功");
    }

    /**
     * 网址审核
     *
     * @param id       数据主键
     * @param auditFlg 审核标识
     * @return com.goblincwl.cwlweb.common.entity.Result<java.lang.Object>
     * @date 2022/11/21 17:43
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/doWebsiteAudit/{id}/{auditFlg}")
    public Result<Object> doWebsiteAudit(@PathVariable Integer id, @PathVariable Integer auditFlg) {
        boolean updateResult = this.commentService.doWebsiteAudit(id, auditFlg);
        return updateResult ? Result.genSuccess("审核成功") : Result.genFail("审核失败");
    }

    /**
     * 使用白名单内的网址对所有评论进行审核
     *
     * @return 反馈
     * @date 2022/12/8 15:16
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/whiteListReview")
    public Result<Object> whiteListReview() {
        this.commentService.whiteListReview();
        return Result.genSuccess("白名单审核成功");
    }


    /**
     * 取消评论被回复时邮件提醒
     *
     * @param id   评论ID
     * @param code 验证code
     * @return 反馈
     * @date 2022/12/15 9:37
     * @author ☪wl
     */
    @GetMapping("/unsubscribe/{id}")
    public ModelAndView unsubscribe(HttpServletRequest request, @PathVariable("id") Integer id, String code) {
        request.setAttribute("status", "取消订阅");
        Comment comment = this.commentService.getById(id);
        if (comment != null && code.equals(comment.getVerificationCode())) {
            comment.setVerificationCode("Unsubscribe");
            this.commentService.updateById(comment);
            request.setAttribute("typeMessage", "取消订阅成功！");
            request.setAttribute("content", "此评论收到回复时将不再通知您✔️");
            request.setAttribute("message", "从此，关于它的事情不再传递。");
            return new ModelAndView("/message");
        }
        request.setAttribute("typeMessage", "取消订阅失败！");
        request.setAttribute("content", "可能是邮件中的地址错误，或者你已经取消过订阅❌");
        request.setAttribute("message", "或许，我们需要通过正规途径！");

        request.setAttribute("profileUrl", this.keyValueOptionsService.getById("profileUrl").getOptValue());
        request.setAttribute("webMaster", this.keyValueOptionsService.getById("webMaster").getOptValue());
        return new ModelAndView("/message");
    }

}
