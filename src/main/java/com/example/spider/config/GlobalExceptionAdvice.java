package com.example.spider.config;

import com.example.spider.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(10)
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public Result<String> handleException(Exception exception){
        log.error("system error ", exception);
        String message = exception.getMessage();
        return Result.failed(message);
    }

    /**
     * 参数校验不通过
     *
     * @param e BindException
     * @return Result<Void>
     */
    @ExceptionHandler(BindException.class)
    public Result<Void> handlerBindException(BindException e) {
        return Result.failed(this.buildMsg(e.getBindingResult()));
    }

    /**
     * 参数校验不通过
     *
     * @param e MethodArgumentNotValidException
     * @return Result<Void>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return Result.failed(buildMsg(e.getBindingResult()));
    }

    /**
     * 构建参数错误提示信息
     *
     * @param bindingResult BindingResult
     * @return String
     */
    private String buildMsg(BindingResult bindingResult) {
        StringBuilder builder = new StringBuilder(32);
        for (FieldError error : bindingResult.getFieldErrors()) {
            builder.append(", [").append(error.getField()).append(":").append(error.getDefaultMessage()).append("]");
        }
        return builder.substring(2);
    }
}
