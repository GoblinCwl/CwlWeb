package com.goblincwl.cwlweb.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.blog.mapper.BlogTabsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章标签 Service
 *
 * @author ☪wl
 * @date 2021-05-09 0:35
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BlogTabsService extends ServiceImpl<BlogTabsMapper, BlogTabs> {

    private final BlogTabsMapper blogTabsMapper;

}
