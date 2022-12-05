package com.goblincwl.cwlweb.modules.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 键值对配置 Controller
 *
 * @author ☪wl
 * @date 2021-05-05 0:26
 */
@RestController
@RequestMapping(ManagerController.MODULE_PREFIX + "/keyValueOptions")
@RequiredArgsConstructor
public class KeyValueOptionsController extends BaseController<KeyValueOptions> {

    private final KeyValueOptionsService keyValueOptionsService;

    /**
     * 分页主查询
     *
     * @param keyValueOptions 查询条件
     * @return 结果集
     * @date 2021-05-05 00:55:40
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<KeyValueOptions>> list(KeyValueOptions keyValueOptions) {
        QueryWrapper<KeyValueOptions> queryWrapper;
        if (StringUtils.isNotEmpty(keyValueOptions.getOptKey())) {
            String optKey = keyValueOptions.getOptKey();
            keyValueOptions.setOptKey(null);
            queryWrapper = createQueryWrapper(keyValueOptions);
            queryWrapper.like("opt_key", optKey);
        } else {
            keyValueOptions.setOptKey(null);
            queryWrapper = createQueryWrapper(keyValueOptions);
        }
        return new Result<Page<KeyValueOptions>>().success(
                this.keyValueOptionsService.page(
                        createPage(),
                        queryWrapper),
                "成功");
    }

    /**
     * 根据主键查询
     *
     * @param optKey 主键
     * @return 结果
     * @date 2021-05-06 10:52:24
     * @author ☪wl
     */
    @GetMapping("/{optKey}")
    @TokenCheck
    public Result<KeyValueOptions> query(@PathVariable("optKey") String optKey) {
        Result<KeyValueOptions> result = new Result<>();
        if (StringUtils.isNotEmpty(optKey)) {
            return result.success(this.keyValueOptionsService.getById(optKey));
        }
        return result.fail();
    }

    /**
     * 新增
     *
     * @param keyValueOptions 数据对象
     * @return 反馈
     * @date 2021-05-06 10:52:24
     * @author ☪wl
     */
    @PostMapping("/add")
    public Result<Object> add(KeyValueOptions keyValueOptions) {
        this.keyValueOptionsService.save(keyValueOptions);
        return Result.genSuccess("添加成功");
    }

    /**
     * 修改
     *
     * @param keyValueOptions 数据对象
     * @return 反馈
     * @date 2021-05-06 10:54:01
     * @author ☪wl
     */
    @PutMapping("/edit")
    public Result<Object> edit(KeyValueOptions keyValueOptions) {
        this.keyValueOptionsService.updateById(keyValueOptions);
        return Result.genSuccess("修改成功");
    }

}
