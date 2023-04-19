package com.example.spider.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_fucai")
public class FuCai extends Model<FuCai> {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String date;
    private String week;
    private String red;
    private String blue;
}
