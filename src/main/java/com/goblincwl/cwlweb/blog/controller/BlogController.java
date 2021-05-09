package com.goblincwl.cwlweb.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.blog.entity.Blog;
import com.goblincwl.cwlweb.blog.service.BlogService;
import com.goblincwl.cwlweb.blog.service.BlogTabsService;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;


/**
 * 博客（文章）Controller
 *
 * @author ☪wl
 * @date 2021-05-07 18:15
 */
@RestController
@RequestMapping(BlogController.MODULE_PREFIX)
@RequiredArgsConstructor
public class BlogController extends BaseController<Blog> {

    public final static String MODULE_PREFIX = "/blog";
    private final BlogService blogService;
    private final BlogTabsService blogTabsService;

    /**
     * 主查询
     *
     * @param blog 参数
     * @return 结果集
     * @date 2021-05-07 18:57:38
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<Blog>> list(Blog blog) {
        QueryWrapper<Blog> queryWrapper = createQueryWrapper(blog);
        queryWrapper.select(Blog.class, info -> !"content".equals(info.getColumn()));
        Page<Blog> page = this.blogService.page(createPage(), queryWrapper);
        //标签赋值
        for (Blog record : page.getRecords()) {
            Integer[] tabsArray = record.getTabsArray();
            if (tabsArray != null) {
                record.setBlogTabsList(this.blogTabsService.listByIds(Arrays.asList(tabsArray)));
            }
        }
        return new Result<Page<Blog>>().success(page);
    }

    /**
     * 根据ID查询
     *
     * @param id 主键
     * @return 结果
     * @date 2021-05-07 22:39:29
     * @author ☪wl
     */
    @GetMapping("/{id}")
    public Result<Blog> one(@PathVariable("id") String id) {
        Blog blog = this.blogService.getById(id);
        if (blog != null) {
            //标签赋值
            Integer[] tabsArray = blog.getTabsArray();
            if (tabsArray != null) {
                blog.setBlogTabsList(this.blogTabsService.listByIds(Arrays.asList(tabsArray)));
            }
        }
        return new Result<Blog>().success(blog, "成功");
    }

    /**
     * 新增
     *
     * @param blog 数据
     * @return 反馈
     * @date 2021-05-07 22:59:10
     * @author ☪wl
     */
    @TokenCheck
    @PostMapping("/add")
    public Result<Object> add(Blog blog) {

        this.blogService.save(blog);
        return Result.genSuccess("添加成功");
    }

    /**
     * 修改
     *
     * @param blog 数据
     * @return 反馈
     * @date 2021-05-07 22:59:21
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/edit")
    public Result<Object> edit(Blog blog) {
        blog.setUpdateTime(new Date());
        this.blogService.updateById(blog);
        return Result.genSuccess("修改成功");
    }

    /**
     * 删除
     *
     * @param ids 主键（逗号拼接）
     * @return 反馈
     * @date 2021-05-07 22:59:30
     * @author ☪wl
     */
    @TokenCheck
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        //TODO 逻辑删除
        if (StringUtils.isNotEmpty(ids)) {
            this.blogService.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.genSuccess("删除成功");
    }
}
