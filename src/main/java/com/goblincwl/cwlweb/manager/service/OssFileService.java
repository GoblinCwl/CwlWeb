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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
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
     * @param ossFile        OSS数据对象
     * @param inputStream    文件流
     * @param objectMetadata 附加信息
     * @return OSS文件名
     * @date 2021-05-09 23:47:47
     * @author ☪wl
     */
    public OssFile uploadFile(OssFile ossFile,
                              InputStream inputStream,
                              ObjectMetadata objectMetadata) {
        //文件后缀分隔符
        String suffixSplit = ".";
        //原始文件名
        String ossFileName = ossFile.getOssFileName();
        String originFileName = ossFile.getOriginFileName();
        if (originFileName.lastIndexOf(suffixSplit) > 0) {
            //如果有后缀，需要加后缀
            String suffix = originFileName.substring(originFileName.lastIndexOf(suffixSplit));
            ossFileName = ossFileName + suffix;
            ossFile.setSuffix(suffix);
        }

        //最终OSS文件名：追加存储路径(oss根据路径上传)
        String storagePath = ossFile.getPath();
        ossFileName = StringUtils.isNotEmpty(storagePath) ? storagePath + "/" + ossFileName : ossFileName;
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
     * 使用UUID作为文件名
     * 上传文件
     *
     * @param file    文件
     * @param ossFile oss文件对象
     * @return OSS文件名
     * @date 2021-04-30 16:17:31
     * @author ☪wl
     */
    public OssFile uploadFile(MultipartFile file, OssFile ossFile) throws IOException {
        //唯一文件名，保存在OSS服务器上的名字
        ossFile.setOssFileName(UUID.randomUUID().toString());
        ossFile.setOriginFileName(file.getOriginalFilename());

        //读取文件流
        InputStream inputStream = file.getInputStream();

        //设置附加信息
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //设置数据流里有多少个字节可以读取
        objectMetadata.setContentLength(inputStream.available());
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentDisposition("inline;filename=" + ossFile.getOssFileName());

        return this.uploadFile(ossFile, inputStream, objectMetadata);
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

    /**
     * 将url图片转为file
     *
     * @param url 图片url
     * @return File
     */
    public File getUrlFile(String url) {
        //读取图片类型
        String fileName = url.substring(url.lastIndexOf("."));
        File file = null;

        URL urlFile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            file = File.createTempFile("oss_tmp_file_", fileName);
            //获取文件
            urlFile = new URL(url);
            inStream = urlFile.openStream();
            os = Files.newOutputStream(file.toPath());

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    /**
     * File转MultipartFile
     *
     * @param file File
     * @return MultipartFile
     * @date 2022/11/24 10:06
     * @author ☪wl
     */
    public MultipartFile getMultipartFile(File file) {
        FileItem item = new DiskFileItemFactory().createItem("file"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , file.getName());
        try (InputStream input = Files.newInputStream(file.toPath());
             OutputStream os = item.getOutputStream()) {
            // 流转移
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        return new CommonsMultipartFile(item);
    }
}
