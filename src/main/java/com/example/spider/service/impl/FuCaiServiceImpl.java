package com.example.spider.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spider.domain.FuCai;
import com.example.spider.mapper.FuCaiMapper;
import com.example.spider.service.IFuCaiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class FuCaiServiceImpl extends ServiceImpl<FuCaiMapper, FuCai> implements IFuCaiService {

    private static final String FUCAI_URL = "http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?name=ssq&systemType=PC&dayStart=2019-01-01&dayEnd=2023-12-01&pageNo=1&pageSize=100";
    @Override
    public boolean fetch(String content) {
//        String fetchUrl = FUCAI_URL + "&pageNo=" + pageNo + "&pageSize=" + pageSize;
        String resultStr = "";
        JSONObject resultJson;
        try {
//            resultStr= HttpUtil.get(fetchUrl, new HashMap<>());
            resultStr = content;
            resultJson = JSON.parseObject(resultStr);
        }catch (Exception e) {
            log.warn("FuCaiService fetch error url ={},result={}", FUCAI_URL, resultStr);
            return false;
        }
        if (null == resultJson || 0 != resultJson.getIntValue("code")) {
            log.warn("FuCaiService fetch code error url ={},result={}", FUCAI_URL, resultJson.toJSONString());
            return false;
        }

        int total = resultJson.getIntValue("total");
        int pageNum = resultJson.getIntValue("pageNum");
        List<FuCai> fuCais = new ArrayList<>();
        JSONArray dataArray = resultJson.getJSONArray("result");
        for(int i = 0; i < dataArray.size(); i++) {
            JSONObject data = dataArray.getJSONObject(i);
            String code = data.getString("code");
            FuCai origin = this.getOne(Wrappers.<FuCai>lambdaQuery().eq(FuCai::getCode, code), false);
            if (origin != null) {
                continue;
            }
            FuCai fuCai = new FuCai();
            fuCai.setCode(code);
            fuCai.setDate(data.getString("date").substring(0, data.getString("date").length()-3));
            fuCai.setWeek("星期" + data.getString("week"));
            fuCai.setRed(data.getString("red"));
            fuCai.setBlue(data.getString("blue"));
            fuCais.add(fuCai);
        }
        this.saveBatch(fuCais);

//        if(pageNo < pageNum) {
//            pageNo++;
//            return this.fetch(pageNo,pageSize);
//        }
        return true;
    }
}
