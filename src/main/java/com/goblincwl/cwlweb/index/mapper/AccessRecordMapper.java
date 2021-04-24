package com.goblincwl.cwlweb.index.mapper;

import com.goblincwl.cwlweb.index.entity.AccessRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问记录 Mapper
 *
 * @author ☪wl
 * @date 2021-04-24 18:35
 */
@Mapper
public interface AccessRecordMapper {
    AccessRecord selectOne(AccessRecord accessRecord);

    Integer insertOne(AccessRecord accessRecord);

    Integer updateOne(AccessRecord accessRecord);

    Integer selectAccessCount();

    Integer selectAccessIpCount();
}
