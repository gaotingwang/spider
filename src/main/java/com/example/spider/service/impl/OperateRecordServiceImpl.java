package com.example.spider.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.spider.domain.OperateRecord;
import com.example.spider.service.IOperateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OperateRecordServiceImpl implements IOperateRecordService {

    @Override
    public boolean add(OperateRecord operateRecord) {
        log.info("add OperateRecord: {}", JSON.toJSONString(operateRecord));
        return true;
    }
}
