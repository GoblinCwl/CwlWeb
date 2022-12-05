package com.goblincwl.cwlweb.modules.manager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.modules.manager.mapper.KeyValueOptionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 键值对配置 Service
 *
 * @author ☪wl
 * @date 2021-05-03 14:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class KeyValueOptionsService extends ServiceImpl<KeyValueOptionsMapper, KeyValueOptions> {

    private final KeyValueOptionsMapper keyValueOptionsMapper;
}
