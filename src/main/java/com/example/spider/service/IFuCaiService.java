package com.example.spider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spider.common.PageResult;
import com.example.spider.domain.FuCai;

public interface IFuCaiService extends IService<FuCai> {

    boolean fetch(String content);

    PageResult<FuCai> findPage(long pageNo, long pageSize);
}
