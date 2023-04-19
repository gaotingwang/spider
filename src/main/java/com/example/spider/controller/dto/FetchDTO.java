package com.example.spider.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FetchDTO {
    @NotBlank(message = "内容不能为空")
    private String content;
}
