package com.goblincwl.cwlweb.manager.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 阿里云OSS文件 Entity
 *
 * @author ☪wl
 * @date 2021-05-09 23:40
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OssFile {

    /**
     * OSS文件名
     */
    @TableId(type = IdType.INPUT)
    private String ossFileName;
    /**
     * 原始文件名
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String originFileName;
    /**
     * 文件路径
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String path;
    /**
     * 文件后缀(.开头)
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String suffix;
    /**
     * 完整URL地址
     */
    private String fullUrl;

}
