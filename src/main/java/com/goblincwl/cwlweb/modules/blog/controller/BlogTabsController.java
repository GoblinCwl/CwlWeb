package com.goblincwl.cwlweb.modules.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.modules.blog.service.BlogTabsService;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 文章标签 Controller
 *
 * @author ☪wl
 * @date 2021-05-09 0:36
 */
@RestController
@RequestMapping(BlogController.MODULE_PREFIX + "/blogTabs")
@RequiredArgsConstructor
public class BlogTabsController extends BaseController<BlogTabs> {

    private final BlogTabsService blogTabsService;

    /**
     * 分页主查询
     *
     * @param blogTabs 条件参数
     * @return 数据集
     * @date 2021-05-09 00:38:48
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<BlogTabs>> list(BlogTabs blogTabs) {
        return new Result<Page<BlogTabs>>().success(
                this.blogTabsService.page(
                        createPage(),
                        createQueryWrapper(blogTabs)
                ), "成功");
    }

    /**
     * 不分页主查询
     *
     * @param blogTabs 条件参数
     * @return 数据集
     * @date 2022/11/11 14:18
     * @author ☪wl
     */
    @GetMapping("/listNoPage")
    public Result<List<BlogTabs>> listNoPage(BlogTabs blogTabs) {
        return new Result<List<BlogTabs>>().success(
                this.blogTabsService.list(new LambdaQueryWrapper<>(blogTabs)
                        .orderByAsc(BlogTabs::getSort1,BlogTabs::getSort2)),
                "成功");
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 数据
     * @date 2021-05-09 00:49:35
     * @author ☪wl
     */
    @GetMapping("/{id}")
    public Result<BlogTabs> one(@PathVariable("id") String id) {
        return new Result<BlogTabs>().success(this.blogTabsService.getById(id), "成功");
    }

    /**
     * 新增
     *
     * @param blogTabs 数据参数
     * @return 反馈
     * @date 2021-05-09 00:51:26
     * @author ☪wl
     */
    @TokenCheck
    @PostMapping("/add")
    public Result<Object> add(BlogTabs blogTabs) {
        this.blogTabsService.save(blogTabs);
        return Result.genSuccess("添加成功");
    }

    /**
     * 修改
     *
     * @param blogTabs 数据参数
     * @return 反馈
     * @date 2021-05-09 00:50:46
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/edit")
    public Result<Object> edit(BlogTabs blogTabs) {
        this.blogTabsService.updateById(blogTabs);
        return Result.genSuccess("修改成功");
    }

    /**
     * 删除
     *
     * @param ids 主键(逗号拼接)
     * @return 反馈
     * @date 2021-05-09 00:52:20
     * @author ☪wl
     */
    @TokenCheck
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            this.blogTabsService.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.genSuccess("删除成功");
    }

}
