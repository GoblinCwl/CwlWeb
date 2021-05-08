package com.goblincwl.cwlweb.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.manager.entity.AccessRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问记录 Mapper
 *
 * @author ☪wl
 * @date 2021-04-24 18:35
 */
@Mapper
public interface AccessRecordMapper extends BaseMapper<AccessRecord> {
    /**
     * 查询访问总数
     *
     * @return 访问总数
     * @date 2021-05-03 14:22:23
     * @author ☪wl
     */
    Integer selectAccessCount();

    /**
     * 查询IP访问总数
     *
     * @return IP访问总数
     * @date 2021-05-03 14:22:34
     * @author ☪wl
     */
    Integer selectAccessIpCount();
}
