package com.goblincwl.cwlweb.manager.controller;

import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.index.entity.AccessRecord;
import com.goblincwl.cwlweb.index.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 访问记录 Controller
 *
 * @author ☪wl
 * @date 2021-05-05 19:36
 */
@RestController
@RequestMapping("/manager/accessRecord")
@RequiredArgsConstructor
public class AccessRecordController extends BaseController<AccessRecord> {

    private final AccessRecordService accessRecordService;

    /**
     * 分页主查询
     *
     * @param accessRecord 查询条件
     * @return 结果集
     * @date 2021-05-05 00:55:40
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Object> list(AccessRecord accessRecord) {
        return Result.genSuccess(this.accessRecordService.page(createPage(), createQueryWrapper(accessRecord)), "成功");
    }

}
