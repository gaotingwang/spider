package com.example.spider.operate;

import com.example.spider.domain.enums.OperateTypeEnum;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OperateLog {

    /**
     * 操作类型
     */
    OperateTypeEnum type();

    /**
     * 操作描述
     */
    String desc() default "";
}
