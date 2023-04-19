package com.example.spider.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spider.domain.FuCai;

public interface IFuCaiService extends IService<FuCai> {

    boolean fetch(String content);
}
