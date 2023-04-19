package com.example.spider.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.spider.common.Result;
import com.example.spider.controller.dto.FetchDTO;
import com.example.spider.domain.FuCai;
import com.example.spider.service.IFuCaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/fucai")
public class FuCaiController {
    @Autowired
    private IFuCaiService fuCaiService;

    @GetMapping("/list")
    public Result<List<FuCai>> getAll() {
        return Result.succeed(fuCaiService.list(Wrappers.<FuCai>lambdaQuery().orderByDesc(FuCai::getId)));
    }

    @PostMapping("/fetch")
    public Result<Boolean> fetch(@RequestBody @Valid FetchDTO fetchDTO) {
        return Result.succeed(fuCaiService.fetch(fetchDTO.getContent()));
    }
}
