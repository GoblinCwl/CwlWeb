package com.goblincwl.cwlweb.manager.controller;

import com.goblincwl.cwlweb.common.entity.Result;
import com.goblincwl.cwlweb.manager.entity.OssFile;
import com.goblincwl.cwlweb.manager.service.OssFileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
public class OssFileController {

    private final Logger logger = LoggerFactory.getLogger(OssFileController.class);
    private final OssFileService ossFileService;

    /**
     * 文件上传
     *
     * @param file        文件
     * @param storagePath 存储路径
     * @return 自定义的返回格式
     * {
     * success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
     * message : "提示的信息，上传成功或上传失败及错误信息等。",
     * url     : "图片地址"        // 上传成功时才返回
     * }
     * @date 2021-05-10 00:20:54
     * @author ☪wl
     */
    @RequestMapping("/uploadFile")
    public Map<String, Object> uploadFile(@RequestParam("editormd-image-file") MultipartFile file, String storagePath) {
        Map<String, Object> resultMap = new HashMap<>(3);
        try {
            OssFile ossFile = this.ossFileService.uploadFile(file, storagePath);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功");
            resultMap.put("url", ossFile.getFullUrl());
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败");
        }
        return resultMap;
    }
}
