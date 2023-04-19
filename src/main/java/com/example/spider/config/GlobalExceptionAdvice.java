package com.example.spider.config;

import com.example.spider.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<String> handleBindException(MethodArgumentNotValidException exception){
        log.error("bind error ", exception);
        BindingResult bindingResult = exception.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        return Result.failed(fieldError.getDefaultMessage());
    }
}
