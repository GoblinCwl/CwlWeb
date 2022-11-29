package com.goblincwl.cwlweb.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.manager.entity.AccessLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 访问日志 Mapper
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/28 09:06
 */
@Mapper
public interface AccessLogMapper extends BaseMapper<AccessLog> {
}
