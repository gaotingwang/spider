package com.example.spider.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spider.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper层
 * @author gtw
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // IPage<User> page(@Param("qry") User qry, Page<User> page); 也可
    @InterceptorIgnore(tenantLine = "true") // 此注解可以让查询时不带tenant_id
    List<User> page(@Param("qry") User qry, Page<User> page);
}