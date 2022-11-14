package com.goblincwl.cwlweb.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.goblincwl.cwlweb.blog.entity.Blog;
import com.goblincwl.cwlweb.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.blog.service.BlogService;
import com.goblincwl.cwlweb.blog.service.BlogTabsService;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
        //查询条件
        String queryInput = blog.getQueryInput();
        //自定义ON条件，无法使用lambda的wrapper
        MPJQueryWrapper<Blog> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.leftJoin("blog_tabs t1 on find_in_set(t1.id,t.tabs)");
        queryWrapper.select(Blog.class, info -> !"content".equals(info.getColumn()));
        queryWrapper.select("t.id");
        queryWrapper.groupBy("t.title", "t.release_time", "t.update_time", "t.tabs", " t.short_content", "t.id", "t.do_archive");
        String sortName = ServletUtils.getParameter("sortName");
        String sortOrder = ServletUtils.getParameter("sortOrder");
        queryWrapper.orderBy(true, "asc".equals(sortOrder), sortName);
        if (StringUtils.isNotEmpty(queryInput)) {
            for (String str : queryInput.split(",")) {
                //带#号查询标签
                if (str.contains("#")) {
                    queryWrapper.or().like("t1.name", str.replaceAll("#", ""));
                } else {
                    queryWrapper.or().like("t.title", str);
                }
            }
        }
        if (blog.getDoArchive() != null) {
            queryWrapper.eq("t.do_archive", blog.getDoArchive());
        }

        Page<Blog> page = this.blogService.page(createPage(), queryWrapper);
        //标签赋值
        for (Blog record : page.getRecords()) {
            Integer[] tabsArray = record.getTabsArray();
            if (tabsArray != null) {
                record.setBlogTabsList(this.blogTabsService.list(
                        new LambdaQueryWrapper<BlogTabs>()
                                .in(BlogTabs::getId, Arrays.asList(tabsArray))
                                .orderByAsc(true, BlogTabs::getSort1)
                                .orderByAsc(true, BlogTabs::getSort2)
                ));
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
    @GetMapping("/query/{id}")
    public Result<Blog> query(@PathVariable("id") String id) {
        Blog blog = this.blogService.getById(id);
        if (blog != null) {
            //标签赋值
            Integer[] tabsArray = blog.getTabsArray();
            if (tabsArray != null) {
                blog.setBlogTabsList(this.blogTabsService.listByIds(Arrays.asList(tabsArray)));
            }
            return new Result<Blog>().success(blog, "成功");
        }
        throw new GoblinCwlException("没有这篇文章");
    }

    /**
     * 实现RestFull风格页面
     * 主要是因为页面跳转全部由/redirect转发
     *
     * @param id 文章ID
     * @return modelAndView
     * @date 2021-05-10 06:59:09
     * @author ☪wl
     */
    @GetMapping("/content/{id}")
    public ModelAndView content(@PathVariable("id") String id) throws ServletException, IOException {
        return new ModelAndView("blog/content", Collections.singletonMap("id", id));
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

    /**
     * 归档
     *
     * @param id 数据主键
     * @return 反馈
     * @date 2022/11/14 10:51
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/doArchive/{id}/{doArchive}")
    public Result<Object> doArchive(@PathVariable Integer id, @PathVariable Integer doArchive) {
        boolean updateResult = this.blogService.update(
                new UpdateWrapper<Blog>()
                        .lambda().eq(Blog::getId, id)
                        .set(id != null, Blog::getDoArchive, doArchive)
                        .set(id != null,Blog::getUpdateTime,new Date())
        );
        return updateResult ? Result.genSuccess("归档成功") : Result.genFail("归档失败");
    }
}
