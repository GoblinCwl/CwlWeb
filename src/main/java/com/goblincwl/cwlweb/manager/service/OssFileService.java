package com.goblincwl.cwlweb.manager.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.common.entity.GoblinCwlConfig;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.manager.entity.OssFile;
import com.goblincwl.cwlweb.manager.mapper.OssFileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * 阿里云OSS文件 Service
 *
 * @author ☪wl
 * @date 2021-05-09 23:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class OssFileService extends ServiceImpl<OssFileMapper, OssFile> {

    private final Logger logger = LoggerFactory.getLogger(OssFileService.class);
    private final OSS oss;
    private final OssFileMapper ossFileMapper;
    private final GoblinCwlConfig goblinCwlConfig;

    /**
     * 上传文件
     *
     * @param originalFilename 原始文件名
     * @param storagePath      存储路径
     * @param ossFileName      OSS文件名
     * @param inputStream      文件流
     * @param objectMetadata   附加信息
     * @return OSS文件名
     * @date 2021-05-09 23:47:47
     * @author ☪wl
     */
    public OssFile uploadFile(String originalFilename,
                              String storagePath,
                              String ossFileName,
                              InputStream inputStream,
                              ObjectMetadata objectMetadata) {
        OssFile ossFile = new OssFile();
        //文件后缀分隔符
        String suffixSplit = ".";
        //原始文件名
        if (originalFilename.lastIndexOf(suffixSplit) > 0) {
            //如果有后缀，需要加后缀
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(suffixSplit));
            ossFileName = ossFileName + suffix;
            ossFile.setSuffix(suffix);
        }
        ossFile.setOriginFileName(originalFilename);

        //最终OSS文件名：追加存储路径(oss根据路径上传)
        ossFileName = StringUtils.isNotEmpty(storagePath) ? storagePath + "/" + ossFileName : ossFileName;
        ossFile.setPath(storagePath);
        ossFile.setOssFileName(ossFileName);

        //最终URL
        ossFile.setFullUrl("https://"
                + goblinCwlConfig.getOssDomain()
                + "/" + ossFileName);

        //先保存至数据库，如果名称重复（主键或者唯一键约束），即可抛出异常，阻止上传
        this.ossFileMapper.insert(ossFile);

        //上传文件
        oss.putObject(
                this.goblinCwlConfig.getOssBucket(),
                ossFileName,
                inputStream,
                objectMetadata);
        return ossFile;
    }

    /**
     * 文件流的方式上传文件
     *
     * @param originalFilename 原始文件名
     * @param inputStream      文件流
     * @param contentType      contentType
     * @param storagePath      存储路径
     * @param ossFileName      OSS文件名
     * @return OSS文件名
     * @date 2021-04-30 17:54:15
     * @author ☪wl
     */
    public OssFile uploadFile(String originalFilename,
                              String storagePath,
                              String ossFileName,
                              InputStream inputStream,
                              String contentType) throws IOException {

        //设置附加信息
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //设置数据流里有多少个字节可以读取
        objectMetadata.setContentLength(inputStream.available());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentDisposition("inline;filename=" + ossFileName);

        return this.uploadFile(originalFilename, storagePath, ossFileName, inputStream, objectMetadata);
    }

    /**
     * 上传文件（半封装）
     *
     * @param file        文件对象
     * @param storagePath 存储路径
     * @param ossFileName OSS文件名
     * @return OSS文件名
     * @date 2021-05-10 00:11:16
     * @author ☪wl
     */
    public OssFile uploadFile(MultipartFile file, String storagePath, String ossFileName) throws IOException {
        //读取文件流
        InputStream inputStream = file.getInputStream();

        //设置附加信息
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //设置数据流里有多少个字节可以读取
        objectMetadata.setContentLength(inputStream.available());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentDisposition("inline;filename=" + ossFileName);

        //文件上传
        return this.uploadFile(Objects.requireNonNull(file.getOriginalFilename()), storagePath, ossFileName, inputStream, objectMetadata);
    }

    /**
     * 使用UUID作为文件名
     * 上传文件
     *
     * @param file        文件
     * @param storagePath 存储路径
     * @return OSS文件名
     * @date 2021-04-30 16:17:31
     * @author ☪wl
     */
    public OssFile uploadFile(MultipartFile file, String storagePath) throws IOException {
        //唯一文件名，保存在OSS服务器上的名字
        return this.uploadFile(file, storagePath, UUID.randomUUID().toString());
    }

    /**
     * 上传多个文件
     *
     * @param files       文件集
     * @param storagePath 存储地址
     * @return 文件名
     * @date 2021-04-29 17:07:40
     * @author ☪wl
     */
    public List<OssFile> uploadFiles(MultipartFile[] files, String storagePath) throws IOException {
        List<OssFile> fileList = new ArrayList<>();
        for (MultipartFile file : files) {
            fileList.add(this.uploadFile(file, storagePath));
        }
        return fileList;
    }

    /**
     * 删除文件
     *
     * @param ossFileName OSS文件名
     * @date 2021-04-30 11:05:19
     * @author ☪wl
     */
    public void removeById(String ossFileName) {
        //删除数据库数据
        this.ossFileMapper.deleteById(ossFileName);
        this.oss.deleteObject(this.goblinCwlConfig.getOssBucket(), ossFileName);
    }

    /**
     * 下载文件
     *
     * @param outputStream 输出流
     * @param ossFileName  OSS文件名
     * @date 2021-04-30 14:27:47
     * @author ☪wl
     */
    public void downloadFile(OutputStream outputStream, String ossFileName) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            OSSObject ossObject = this.oss.getObject(this.goblinCwlConfig.getOssBucket(), ossFileName);
            // 读取文件内容。
            in = new BufferedInputStream(ossObject.getObjectContent());
            out = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
        } catch (Exception e) {
            logger.error("OSS文件下载失败", e);
            throw new GoblinCwlException("OSS文件下载失败");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

}
