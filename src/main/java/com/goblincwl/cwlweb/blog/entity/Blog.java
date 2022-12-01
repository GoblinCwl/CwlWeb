package com.goblincwl.cwlweb.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.goblincwl.cwlweb.common.utils.ConvertUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 博客（文章）Entity
 *
 * @author ☪wl
 * @date 2021-05-07 17:58
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {

    private static final long serialVersionUID = 5931944009459656289L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 文章标题
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布时间
     */
    @TableField(whereStrategy = FieldStrategy.NOT_NULL)
    private Date releaseTime;

    /**
     * 更新时间
     */
    @TableField(whereStrategy = FieldStrategy.NOT_NULL)
    private Date updateTime;

    /**
     * 文章标签
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String tabs;

    /**
     * 标签数组
     */
    @TableField(exist = false)
    private Integer[] tabsArray;

    /**
     * 标签集合
     */
    @TableField(exist = false)
    private List<BlogTabs> blogTabsList;

    /**
     * 预览内容
     */
    private String shortContent;

    /**
     * 是否归档
     */
    private Integer doArchive;

    /**
     * 浏览次数
     */
    private Long browserTimes;

    /**
     * 是否归档(页面查询)
     */
    @TableField(exist = false)
    private String doArchiveStr;

    /**
     * 查询条件
     */
    @TableField(exist = false)
    private String queryInput;

    public String getTabs() {
        if (StringUtils.isEmpty(this.tabs)) {
            if (this.tabsArray != null && this.tabsArray.length > 0) {
                return StringUtils.join(this.getTabsArray(), ",");
            }
        }
        return this.tabs;
    }

    public Integer[] getTabsArray() {
        if (this.tabsArray == null || this.tabsArray.length <= 0) {
            if (StringUtils.isNotEmpty(this.tabs)) {
                return ConvertUtils.toIntArray(this.tabs);
            }
        }
        return tabsArray;
    }
}
