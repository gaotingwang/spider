package com.example.spider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spider.domain.TiCai;

public interface ITiCaiService extends IService<TiCai> {

    boolean fetch(int pageNo, int pageSize);
}
