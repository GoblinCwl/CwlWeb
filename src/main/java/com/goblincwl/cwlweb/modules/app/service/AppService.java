package com.goblincwl.cwlweb.modules.app.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.app.entitiy.App;
import com.goblincwl.cwlweb.modules.app.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 应用 Service
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 16:53
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AppService extends ServiceImpl<AppMapper, App> {
    /**
     * 根据ID查询图标文件ID集合
     * @param idList ID集合
     * @return 图标文件ID集合
     * @date 2022/12/8 14:09
     * @author ☪wl
     */
    public List<String> iconFileListByIds(List<String> idList) {
        return this.baseMapper.selectIconFileListByIds(idList);
    }
}
