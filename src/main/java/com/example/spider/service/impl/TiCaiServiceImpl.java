package com.example.spider.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spider.domain.TiCai;
import com.example.spider.mapper.TiCaiMapper;
import com.example.spider.service.ITiCaiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class TiCaiServiceImpl extends ServiceImpl<TiCaiMapper, TiCai> implements ITiCaiService {

    private static final String TICAI_URL = "https://webapi.sporttery.cn/gateway/lottery/getHistoryPageListV1.qry?gameNo=85&provinceId=0&isVerify=1&startTerm=20000&endTerm=23042";
    @Override
    public boolean fetch(int pageNo, int pageSize) {
        String fetchUrl = TICAI_URL + "&pageNo=" + pageNo + "&pageSize=" + pageSize;
        String resultStr = "";
        JSONObject resultJson;
        try {
            resultStr= HttpUtil.get(fetchUrl, new HashMap<>());
            resultJson = JSON.parseObject(resultStr);
        }catch (Exception e) {
            log.warn("FuCaiService fetch error url ={},result={}", fetchUrl, resultStr);
            return false;
        }
        if (null == resultJson || 0 != resultJson.getIntValue("errorCode")) {
            log.warn("FuCaiService fetch code error url ={},result={}", fetchUrl, resultJson.toJSONString());
            return false;
        }
        resultJson = resultJson.getJSONObject("value");

        int total = resultJson.getIntValue("total");
        int pageNum = resultJson.getIntValue("pages");
        List<TiCai> tiCais = new ArrayList<>();
        JSONArray dataArray = resultJson.getJSONArray("list");
        for(int i = 0; i < dataArray.size(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            String code = data.getString("lotteryDrawNum");

            String nums = data.getString("lotteryDrawResult");
            String[] codes = nums.split(" ");
            List<String> reds = new ArrayList<>(5);
            List<String> blues = new ArrayList<>(2);
            for(int j = 0; j < 5; j++) {
                reds.add(codes[j]);
            }
            for(int j = 5; j < codes.length; j++) {
                blues.add(codes[j]);
            }
            String red = String.join(",", reds);
            String blue = String.join(",", blues);
            TiCai origin = this.getOne(Wrappers.<TiCai>lambdaQuery().eq(TiCai::getCode, code), false);
            if (origin != null) {
                continue;
            }
            TiCai tiCai = new TiCai();
            tiCai.setCode(code);
            tiCai.setDate(data.getString("lotteryDrawTime"));
            tiCai.setWeek("星期");
            tiCai.setRed(red);
            tiCai.setBlue(blue);
            tiCais.add(tiCai);
        }
        this.saveBatch(tiCais);

        if(pageNo < pageNum) {
            pageNo++;
            return this.fetch(pageNo,pageSize);
        }
        return true;
    }
}
