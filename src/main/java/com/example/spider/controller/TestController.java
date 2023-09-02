package com.example.spider.controller;

import com.example.spider.domain.User;
import com.example.spider.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/insert")
    public void insert() throws InterruptedException {
        User user = new User(null, "Tom", 1, "tom@qq.com", null, null, null);
        userMapper.insert(user);
        User beforeUser = userMapper.selectById(user.getId());
        log.info("before user:{}", beforeUser);
        Thread.sleep(1 * 1000L);
        beforeUser.setOperator(null);
        beforeUser.setAge(12);
        userMapper.updateById(beforeUser);
        log.info("query user:{}", userMapper.selectById(user.getId()));
    }

}