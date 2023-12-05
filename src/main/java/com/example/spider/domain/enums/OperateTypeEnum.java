package com.example.spider.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperateTypeEnum {

    /**
     * 任务管理
     */
    TASK_MANAGER(1, "任务管理"),
    ;

    private final Integer code;

    private final String name;
}
