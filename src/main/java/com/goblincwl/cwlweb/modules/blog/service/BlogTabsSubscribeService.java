package com.goblincwl.cwlweb.modules.blog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabsSubscribe;
import com.goblincwl.cwlweb.modules.blog.mapper.BlogTabsSubscribeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文章标签订阅 Service
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/09 15:04
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BlogTabsSubscribeService extends ServiceImpl<BlogTabsSubscribeMapper, BlogTabsSubscribe> {

    /**
     * 根据日期查询合计
     *
     * @param date 日期
     * @return 总数
     * @date 2022/12/9 17:07
     * @author ☪wl
     */
    public Long countByDate(String date) {
        return this.baseMapper.countByDate(date);
    }
}
