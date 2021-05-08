package com.goblincwl.cwlweb.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.manager.entity.BadWords;
import org.apache.ibatis.annotations.Mapper;


/**
 * 敏感词 Mapper
 *
 * @author ☪wl
 * @date 2021-04-25 14:32
 */
@Mapper
public interface BadWordsMapper extends BaseMapper<BadWords> {
}
