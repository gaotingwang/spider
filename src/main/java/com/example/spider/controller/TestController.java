package com.example.spider.controller;

import com.example.spider.domain.User;
import com.example.spider.domain.enums.GradeEnum;
import com.example.spider.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/insert")
    public User insert() throws InterruptedException {
        User user = new User("Tom", 1, "tom@qq.com", GradeEnum.HIGH);
        userMapper.insert(user);
        User beforeUser = userMapper.selectById(user.getId());
        log.info("before user:{}", beforeUser);

        Thread.sleep(1 * 1000L);

        beforeUser.setOperator(null);
        beforeUser.setAge(12);
        userMapper.updateById(beforeUser);
        return userMapper.selectById(user.getId());
    }

    @GetMapping("/user/{id}")
    public User get(@PathVariable Long id) {
        return userMapper.selectById(id);
    }

    @DeleteMapping("/user/{id}")
    public int delete(@PathVariable Long id) {
        return userMapper.deleteById(id);
    }

}