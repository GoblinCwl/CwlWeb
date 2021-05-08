package com.goblincwl.cwlweb.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.blog.entity.Blog;
import com.goblincwl.cwlweb.blog.mapper.BlogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 博客（文章）Service
 *
 * @author ☪wl
 * @date 2021-05-07 18:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BlogService extends ServiceImpl<BlogMapper, Blog> {
}
