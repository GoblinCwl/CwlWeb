package com.goblincwl.cwlweb.manager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.manager.entity.BadWords;
import com.goblincwl.cwlweb.manager.mapper.BadWordsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 敏感词 Service
 *
 * @author ☪wl
 * @date 2021-04-25 14:32
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BadWordsService extends ServiceImpl<BadWordsMapper, BadWords> {

    private final BadWordsMapper badWordsMapper;
}
