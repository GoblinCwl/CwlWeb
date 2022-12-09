package com.goblincwl.cwlweb.modules.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabsSubscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 文章标签订阅 Mapper
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/09 15:04
 */
@Mapper
public interface BlogTabsSubscribeMapper extends BaseMapper<BlogTabsSubscribe> {

    /**
     * 根据日期查询总数
     *
     * @param date 日期
     * @return 总数
     * @date 2022/12/9 17:07
     * @author ☪wl
     */
    Long countByDate(@Param("date") String date);
}
