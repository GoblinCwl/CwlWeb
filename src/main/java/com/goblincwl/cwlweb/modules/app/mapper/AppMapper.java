package com.goblincwl.cwlweb.modules.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.goblincwl.cwlweb.modules.app.entitiy.App;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用 Mapper
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 16:53
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {
    /**
     * 根据ID查询图标文件ID集合
     * @param idList ID集合
     * @return 图标文件ID集合
     * @date 2022/12/8 14:09
     * @author ☪wl
     */
    List<String> selectIconFileListByIds(@Param("idList") List<String> idList);
}
