package com.goblincwl.cwlweb.manager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.manager.entity.AccessLog;
import com.goblincwl.cwlweb.manager.mapper.AccessLogMapper;
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
}
