package com.goblincwl.cwlweb.modules.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.manager.entity.BadWords;
import com.goblincwl.cwlweb.modules.manager.service.BadWordsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 违禁词 Controller
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/10 14:49
 */

@RestController
@RequestMapping(ManagerController.MODULE_PREFIX + "/badWords")
@RequiredArgsConstructor
public class BadWordsController extends BaseController<BadWords> {

    private final BadWordsService badWordsService;

    /**
     * 分页主查询
     *
     * @param badWords 查询参数
     * @return 结果集
     * @date 2022/11/10 14:55
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<BadWords>> list(BadWords badWords) {
        return new Result<Page<BadWords>>().success(
                this.badWordsService.page(
                        createPage(),
                        createQueryWrapper(badWords)),
                "成功");
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 结果
     * @date 2022/11/10 15:51
     * @author ☪wl
     */
    @GetMapping("/{id}")
    public Result<BadWords> query(@PathVariable("id") String id) {
        Result<BadWords> result = new Result<>();
        if (StringUtils.isNotEmpty(id)) {
            return result.success(this.badWordsService.getById(id));
        }
        return result.fail();
    }

    /**
     * 添加违禁词
     *
     * @param badWords 数据对象
     * @return 反馈
     * @date 2022/11/10 15:47
     * @author ☪wl
     */
    @PostMapping(value = "/add")
    public Result<Object> add(BadWords badWords) {
        this.badWordsService.save(badWords);
        return Result.genSuccess("添加成功");
    }

    /**
     * 修改违禁词
     *
     * @param badWords 数据对象
     * @return 反馈
     * @date 2022/11/10 15:52
     * @author ☪wl
     */
    @PutMapping(value = "/edit")
    public Result<Object> edit(BadWords badWords) {
        this.badWordsService.updateById(badWords);
        return Result.genSuccess("修改成功");
    }

    /**
     * 删除违禁词
     *
     * @param ids 主键(逗号拼接)
     * @return 反馈
     * @date 2022/11/10 15:54
     * @author ☪wl
     */
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            this.badWordsService.removeByIds(Arrays.asList(ids.split(",")));
        }
        return Result.genSuccess("删除成功");
    }

}
