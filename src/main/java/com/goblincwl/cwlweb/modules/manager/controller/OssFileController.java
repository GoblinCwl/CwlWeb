package com.goblincwl.cwlweb.modules.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.common.web.controller.BaseController;
import com.goblincwl.cwlweb.modules.manager.entity.OssFile;
import com.goblincwl.cwlweb.modules.manager.service.OssFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云OSS文件 Controller
 *
 * @author ☪wl
 * @date 2021-05-09 23:46
 */
@RestController
@RequestMapping(ManagerController.MODULE_PREFIX + "/ossFile")
@RequiredArgsConstructor
public class OssFileController extends BaseController<OssFile> {

    private final Logger LOG = LoggerFactory.getLogger(OssFileController.class);
    private final OssFileService ossFileService;

    /**
     * 主查询
     *
     * @param ossFile 查询参数
     * @return 结果集
     * @date 2021-05-12 17:57:49
     * @author ☪wl
     */
    @GetMapping("/list")
    public Result<Page<OssFile>> list(OssFile ossFile) {
        QueryWrapper<OssFile> queryWrapper;
        if (StringUtils.isEmpty(ossFile.getOssFileName())) {
            ossFile.setOssFileName(null);
        }
        queryWrapper = createQueryWrapper(ossFile);
        return new Result<Page<OssFile>>().success(
                this.ossFileService.page(
                        createPage(),
                        queryWrapper
                ), "成功"
        );
    }

    /**
     * 删除
     *
     * @param ids OSS文件名（逗号拼接）
     * @return 反馈
     * @date 2021-05-12 22:24:47
     * @author ☪wl
     */
    @DeleteMapping("/remove")
    public Result<Object> remove(String ids) {
        if (StringUtils.isNotEmpty(ids)) {
            for (String ossFileName : ids.split(",")) {
                this.ossFileService.removeById(ossFileName);
            }
        }
        return Result.genSuccess("删除成功");
    }

    /**
     * 文件下载
     *
     * @param ossFileName OSS文件名
     * @param response    响应对象
     * @date 2021-05-12 22:37:29
     * @author ☪wl
     */
    @PostMapping("/download")
    public void download(String ossFileName, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmpty(ossFileName)) {
            this.ossFileService.downloadFile(response.getOutputStream(), ossFileName);
            return;
        }
        throw new GoblinCwlException("文件[" + ossFileName + "]不存在");
    }

    /**
     * editorMd 图片上传
     *
     * @param file 文件
     * @return 自定义的返回格式
     * {
     * success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
     * message : "提示的信息，上传成功或上传失败及错误信息等。",
     * url     : "图片地址"        // 上传成功时才返回
     * }
     * @date 2021-05-10 00:20:54
     * @author ☪wl
     */
    @RequestMapping(value = "/editorMdUploadFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> editorMdUploadFile(@RequestParam("editormd-image-file") MultipartFile file) {
        //博客图片上传
        String storagePath = "blogs";
        Map<String, Object> resultMap = new HashMap<>(3);
        try {
            OssFile ossFile = this.ossFileService.uploadFile(file, OssFile.builder().path(storagePath).build());
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功");
            resultMap.put("url", ossFile.getFullUrl());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败");
        }
        return resultMap;
    }

    /**
     * 应用表单图片上传
     *
     * @param file 文件
     * @return 文件主键
     * @date 2022/12/8 14:08
     * @author ☪wl
     */
    @RequestMapping("/formUploadFile")
    public Result<Object> formUploadFile(@RequestParam("file_data") MultipartFile file, String storagePath) {
        OssFile ossFile = new OssFile();
        try {
            ossFile = this.ossFileService.uploadFile(file, OssFile.builder().path(storagePath).build());
        } catch (IOException e) {
            LOG.error("文件上传失败", e);
        }
        return Result.genSuccess(ossFile, "成功");
    }
}
