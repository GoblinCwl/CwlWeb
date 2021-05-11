package com.goblincwl.cwlweb.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.blog.entity.Comment;
import com.goblincwl.cwlweb.blog.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 评论 Service
 *
 * @author ☪wl
 * @date 2021-05-10 15:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    private final CommentMapper commentMapper;

}
