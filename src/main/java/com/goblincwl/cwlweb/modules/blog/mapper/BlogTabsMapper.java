package com.goblincwl.cwlweb.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 文章标签 Mapper
 *
 * @author ☪wl
 * @date 2021-05-09 0:35
 */
@Mapper
public interface BlogTabsMapper extends BaseMapper<BlogTabs> {

    /**
     * 最热门5个标签
     * @return 结果集
     * @date 2022/12/9 17:22
     * @author ☪wl
     */
    List<BlogTabs> hotBlogTabsList();

}
