package com.goblincwl.cwlweb.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.blog.entity.Blog;
import org.apache.ibatis.annotations.Mapper;


/**
 * 博客（文章）Mapper
 *
 * @author ☪wl
 * @date 2021-05-07 18:11
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {
}
