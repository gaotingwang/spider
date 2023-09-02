package com.example.spider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spider.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper层
 * @author gtw
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}