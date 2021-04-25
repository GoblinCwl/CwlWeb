package com.goblincwl.cwlweb.index.service;

import com.goblincwl.cwlweb.index.entity.BadWords;
import com.goblincwl.cwlweb.index.mapper.BadWordsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 敏感词 Service
 *
 * @author ☪wl
 * @date 2021-04-25 14:32
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BadWordsService {

    private final BadWordsMapper badWordsMapper;

    public List<BadWords> findList() {
        return this.badWordsMapper.selectList();
    }
}
