package com.goblincwl.cwlweb.index.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.index.entity.AccessRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问记录 Mapper
 *
 * @author ☪wl
 * @date 2021-04-24 18:35
 */
@Mapper
public interface AccessRecordMapper extends BaseMapper<AccessRecord> {
    Integer selectAccessCount();

    Integer selectAccessIpCount();
}
