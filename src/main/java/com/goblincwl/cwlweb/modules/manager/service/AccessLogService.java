package com.goblincwl.cwlweb.modules.manager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.manager.entity.AccessLog;
import com.goblincwl.cwlweb.modules.manager.mapper.AccessLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 访问日志 Service
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/28 09:07
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AccessLogService extends ServiceImpl<AccessLogMapper, AccessLog> {

    /**
     * 根据日期查询数量
     * @param date 日期
     * @return 数量
     * @date 2022/11/30 9:49
     * @author ☪wl
     */
    public Long countByDate(String date) {
        return this.baseMapper.countByDate(date);
    }
}
