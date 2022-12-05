package com.goblincwl.cwlweb.modules.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.manager.entity.AccessLog;
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

    /**
     * 日期查询数量
     *
     * @param date 日期
     * @return java.lang.Long 数量
     * @date 2022/11/30 9:50
     * @author ☪wl
     */
    Long countByDate(String date);
}
