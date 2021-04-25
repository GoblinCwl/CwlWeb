package com.goblincwl.cwlweb.index.mapper;

import com.goblincwl.cwlweb.index.entity.BadWords;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 敏感词 Mapper
 *
 * @author ☪wl
 * @date 2021-04-25 14:32
 */
@Mapper
public interface BadWordsMapper {
    public List<BadWords> selectList();
}
