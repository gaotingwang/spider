package com.example.spider.controller.dto;

import com.example.spider.operate.OperateLogInterface;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CmdDTO implements OperateLogInterface {

    @NotBlank
    private String sn;

    @Override
    public String getOperateLog() {
        return "测试记录" + sn + "的操作日志";
    }
}
