package com.example.spider.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.example.spider.domain.enums.GradeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户实体
 * @author gtw
 */
@Data
@TableName(value = "sys_user")
@NoArgsConstructor
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private String email;

    /**
     * 年级，原生枚举（带{@link com.baomidou.mybatisplus.annotation.EnumValue}):
     * 数据库字段：grade INT(2)
     */
    private GradeEnum grade;

    private String tenantId;

    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    @TableField(exist = false)
    private List<Integer> ages;

    public User(String name, Integer age, String email, GradeEnum grade) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.grade = grade;
    }
}