package com.goblincwl.cwlweb.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论 Mapper
 *
 * @author ☪wl
 * @date 2021-05-10 15:37
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 根据博客ID查询评论ID
     *
     * @param blogIdList 博客ID集合
     * @return 评论ID集合
     * @date 2022/12/8 14:01
     * @author ☪wl
     */
    List<Integer> selectIdListByBlogIds(@Param("blogIdList") List<String> blogIdList);
}
