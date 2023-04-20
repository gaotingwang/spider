package com.example.spider.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.spider.common.PageResult;
import com.example.spider.common.Result;
import com.example.spider.controller.dto.FetchDTO;
import com.example.spider.domain.FuCai;
import com.example.spider.domain.TiCai;
import com.example.spider.service.IFuCaiService;
import com.example.spider.service.ITiCaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cai")
public class CaiController {
    @Autowired
    private IFuCaiService fuCaiService;
    @Autowired
    private ITiCaiService tiCaiService;

    @GetMapping("/fu/list")
    public Result<List<FuCai>> getFuCaiAll() {
        return Result.succeed(fuCaiService.list(Wrappers.<FuCai>lambdaQuery().orderByDesc(FuCai::getId)));
    }

    @GetMapping("/fu/page")
    public PageResult<FuCai> getFuCaiPage(@RequestParam long pageNo, @RequestParam long pageSize) {
        return fuCaiService.findPage(pageNo, pageSize);
    }

    @PostMapping("/fu/fetch")
    public Result<Boolean> fetchFuCai(@RequestBody @Valid FetchDTO fetchDTO) {
        return Result.succeed(fuCaiService.fetch(fetchDTO.getContent()));
    }

    @GetMapping("/ti/list")
    public Result<List<TiCai>> getTiCaiAll() {
        return Result.succeed(tiCaiService.list(Wrappers.<TiCai>lambdaQuery().orderByDesc(TiCai::getId)));
    }

    @PostMapping("/ti/fetch")
    public Result<Boolean> fetchTiCai() {
        return Result.succeed(tiCaiService.fetch(1, 100));
    }
}
