package com.example.spider.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> succeed(String msg) {
        return of((T) null, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, ResultCodeEnum.SUCCESS.getCode(), "success");
    }

    public static <T> Result<T> failed(String msg) {
        return of((T) null, ResultCodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return of(model, ResultCodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result(data, code, msg);
    }

    public String toString() {
        return "Result(data=" + this.getData() + ", code=" + this.getCode() + ", msg=" + this.getMsg() + ")";
    }

}