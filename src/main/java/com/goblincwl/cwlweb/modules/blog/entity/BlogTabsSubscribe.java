package com.goblincwl.cwlweb.modules.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章标签订阅 Entity
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/09 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogTabsSubscribe implements Serializable {
    private static final long serialVersionUID = -5564383653738626488L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 订阅邮箱
     */
    private String email;

    /**
     * 标签主键
     */
    private Integer blogTabsId;

    /**
     * 订阅时间
     */
    private Date subscribeTime;

}
