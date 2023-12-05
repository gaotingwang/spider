package com.example.spider.domain;

import com.example.spider.domain.enums.OperateTypeEnum;
import lombok.Data;

@Data
public class OperateRecord {
    /**
     * ip地址
     */
    private String ip;

    /**
     * 操作用户id
     */
    private Long sysUserId;

    /**
     * 操作用户名
     */
    private String sysUserName;

    /**
     * 设备sn号
     */
    private String deviceSn;

    /**
     * 操作类型
     */
    private OperateTypeEnum type;

    /**
     * 操作描述
     */
    private String operatorDesc;

    /**
     * 操作时间
     */
    private Long createTime;
}
