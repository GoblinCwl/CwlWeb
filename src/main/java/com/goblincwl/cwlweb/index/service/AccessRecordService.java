package com.goblincwl.cwlweb.index.service;

import com.goblincwl.cwlweb.index.entity.AccessRecord;
import com.goblincwl.cwlweb.index.mapper.AccessRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 访问记录 Service
 *
 * @author ☪wl
 * @date 2021-04-24 18:34
 */
@Service
@Transactional
public class AccessRecordService {

    private final AccessRecordMapper accessRecordMapper;

    public AccessRecordService(AccessRecordMapper accessRecordMapper) {
        this.accessRecordMapper = accessRecordMapper;
    }

    public AccessRecord findOne(AccessRecord accessRecord) {
        return this.accessRecordMapper.selectOne(accessRecord);
    }

    public Integer saveOne(AccessRecord accessRecord) {
        return this.accessRecordMapper.insertOne(accessRecord);
    }

    public Integer updateOne(AccessRecord accessRecord) {
        return this.accessRecordMapper.updateOne(accessRecord);
    }

    public Integer findAccessCount() {
        return this.accessRecordMapper.selectAccessCount();
    }

    public Integer findAccessIpCount() {
        return this.accessRecordMapper.selectAccessIpCount();
    }
}
