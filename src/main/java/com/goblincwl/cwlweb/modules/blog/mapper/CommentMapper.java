package com.goblincwl.cwlweb.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论 Mapper
 *
 * @author ☪wl
 * @date 2021-05-10 15:37
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
