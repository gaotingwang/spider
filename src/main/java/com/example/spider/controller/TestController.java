package com.example.spider.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spider.common.PageResult;
import com.example.spider.common.Result;
import com.example.spider.controller.dto.CmdDTO;
import com.example.spider.domain.User;
import com.example.spider.domain.enums.GradeEnum;
import com.example.spider.domain.enums.OperateTypeEnum;
import com.example.spider.mapper.UserMapper;
import com.example.spider.operate.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/insert")
    public User insert() throws InterruptedException {
        User user = new User("Tom", 1, "tom@qq.com", GradeEnum.HIGH);
        // 手动设置了tenantId，系统不再重新赋值，如果为空，系统可以自动复制给tenantId
        user.setTenantId("test_user");
        userMapper.insert(user);
        User beforeUser = userMapper.selectById(user.getId());
        log.info("before user:{}", beforeUser);

//        Thread.sleep(1 * 1000L);
//
//        beforeUser.setOperator(null);
//        beforeUser.setAge(12);
//        userMapper.updateById(beforeUser);
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

    @GetMapping("/users")
    public PageResult<User> getPages(@RequestParam int pageNo, @RequestParam int pageSize, @RequestBody User qry) {
        Page<User> page = new Page<>(pageNo, pageSize);
        List<User> result = userMapper.page(qry, page);
        return PageResult.<User>builder().data(result).count(page.getTotal()).build();
    }

    @PostMapping("/records")
//    @OperateLog(type = OperateTypeEnum.TASK_MANAGER, desc = "测试日志记录")
    @OperateLog(type = OperateTypeEnum.TASK_MANAGER)
    public Result<Boolean> test(@RequestBody @Valid CmdDTO cmdDTO) {
        return Result.succeed("success");
    }
}