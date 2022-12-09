package com.goblincwl.cwlweb.modules.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.modules.blog.mapper.BlogTabsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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


    /**
     * 最热门5个标签
     * @return 结果集
     * @date 2022/12/9 17:21
     * @author ☪wl
     */
    public List<BlogTabs> hotBlogTabsList() {
        return this.baseMapper.hotBlogTabsList();
    }
}
