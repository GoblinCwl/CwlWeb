package com.goblincwl.cwlweb.modules.app.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.app.entitiy.App;
import com.goblincwl.cwlweb.modules.app.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
