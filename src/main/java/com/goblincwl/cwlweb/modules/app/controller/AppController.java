package com.goblincwl.cwlweb.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.goblincwl.cwlweb.common.annotation.TokenCheck;
import com.goblincwl.cwlweb.common.annotation.aop.UsesTimes;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.utils.ServletUtils;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.app.entitiy.App;
import com.goblincwl.cwlweb.modules.app.service.AppService;
import com.goblincwl.cwlweb.modules.manager.service.OssFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 应用 Controller
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 16:51
 */
@RestController
@RequestMapping(AppController.MODULE_PREFIX)
@RequiredArgsConstructor
public class AppController extends BaseController<App> {

    public final static String MODULE_PREFIX = "/apps";
    public final static String APP_PREFIX = "/app";

    private final AppService appService;

    private final OssFileService ossFileService;

    /**
     * 分页主查询
     *
     * @param app 查询条件
     * @return 结果集
     * @date 2022/12/5 16:57
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<App>> list(App app) {
        MPJQueryWrapper<App> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.leftJoin("oss_file t1 on t.icon_file = t1.oss_file_name");
        queryWrapper.select(App.class, info -> !"html".equals(info.getColumn()));
        queryWrapper.select("t.id");
        queryWrapper.select("t1.full_url iconUrl", "t1.origin_file_name iconFileName");
        String sortName = ServletUtils.getParameter("sortName");
        String sortOrder = ServletUtils.getParameter("sortOrder");
        queryWrapper.orderBy(true, "asc".equals(sortOrder), sortName);
        //名字查询
        if (StringUtils.isNotEmpty(app.getName())) {
            queryWrapper.like("t.name", app.getName());
        }
        //状态查询
        if (app.getIsLock() != null) {
            queryWrapper.eq("t.is_lock", app.getIsLock());
        }
        //锁定查询条件查询
        if (StringUtils.isNotEmpty(app.getIsLockStr())) {
            queryWrapper.and(wrapper -> {
                for (String isLock : app.getIsLockStr().split(",")) {
                    wrapper.or(innerWrapper -> innerWrapper.eq("t.is_lock", isLock));
                }
            });
        }
        return new Result<Page<App>>().success(
                this.appService.page(
                        createPage(),
                        queryWrapper
                ), "成功");
    }

    /**
     * 根据主键查询
     *
     * @param id 主键ID
     * @return 结果
     * @date 2022/12/5 16:59
     * @author ☪wl
     */
    @GetMapping("/query/{id}")
    public Result<App> one(@PathVariable("id") String id) {
        MPJQueryWrapper<App> queryWrapper = new MPJQueryWrapper<>();
        queryWrapper.leftJoin("oss_file t1 on t.icon_file = t1.oss_file_name");
        queryWrapper.selectAll(App.class);
        queryWrapper.select("t1.full_url iconUrl", "t1.origin_file_name iconFileName");
        queryWrapper.eq("t.id", id);
        return new Result<App>().success(this.appService.getOne(queryWrapper), "成功");
    }

    /**
     * RestFull
     * @param id appId
     * @return modelAndView
     * @date 2022/12/8 14:31
     * @author ☪wl
     */
    @UsesTimes
    @GetMapping("/app/{id}")
    public ModelAndView app(@PathVariable("id") String id) {
        return new ModelAndView("apps/app", Collections.singletonMap("id", id));
    }

    /**
     * 新增
     *
     * @param app 数据参数
     * @return 反馈
     * @date 2022/12/5 17:00
     * @author ☪wl
     */
    @TokenCheck
    @PostMapping("/add")
    public Result<Object> add(App app) {
        this.appService.save(app);
        return Result.genSuccess("添加成功");
    }

    /**
     * 修改
     *
     * @param app 数据参数
     * @return 反馈
     * @date 2022/12/5 17:01
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/edit")
    public Result<Object> edit(App app) {
        this.appService.updateById(app);
        return Result.genSuccess("修改成功");
    }

    /**
     * 删除
     *
     * @param ids 主键ID(逗号拼接)
     * @return 反馈
     * @date 2022/12/5 17:02
     * @author ☪wl
     */
    @TokenCheck
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            List<String> idList = Arrays.asList(ids.split(","));
            //删除图片信息
            List<String> iconFileList = this.appService.iconFileListByIds(idList);
            iconFileList.forEach(this.ossFileService::removeById);
            this.appService.removeByIds(idList);
        }
        return Result.genSuccess("删除成功");
    }

    /**
     * 应用开启/关闭
     *
     * @param id         主键ID
     * @param openStatus 开关状态
     * @return 反馈
     * @date 2022/12/5 17:06
     * @author ☪wl
     */
    @TokenCheck
    @PutMapping("/doOpen/{id}/{openStatus}")
    public Result<Object> doOpen(@PathVariable Integer id, @PathVariable Integer openStatus) {
        boolean updateResult = this.appService.update(
                new UpdateWrapper<App>()
                        .lambda().eq(App::getId, id)
                        .set(id != null, App::getIsLock, openStatus)
        );
        return updateResult ? Result.genSuccess("开关成功") : Result.genSuccess("开关失败");
    }
}
