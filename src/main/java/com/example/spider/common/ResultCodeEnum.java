package com.example.spider.common;

public enum ResultCodeEnum {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    ERROR(0),
    ;

    private final Integer code;

    private ResultCodeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}