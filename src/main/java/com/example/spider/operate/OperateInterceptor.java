package com.example.spider.operate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.spider.domain.OperateRecord;
import com.example.spider.service.IOperateRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class OperateInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IOperateRecordService operateRecordService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerMethod methodHandler = (HandlerMethod) handler;
        OperateLog operateLog = methodHandler.getMethodAnnotation(OperateLog.class);
        if (operateLog == null) {
            return;
        }

        // 判断请求是否成功
        if (response.getStatus() != HttpStatus.OK.value()) {
            return;
        }
        int responseCode = getResponseCode(response);
        if (responseCode != 0) {
            return;
        }

        String requestBody = getRequestBody(request);
        OperateRecord record = new OperateRecord();
        record.setIp(getRemoteIp(request));
        record.setDeviceSn(getDeviceSn(requestBody));
        record.setType(operateLog.type());
        if(StringUtils.isBlank(operateLog.desc())) {
            String desc = getDesc(requestBody, methodHandler);
            record.setOperatorDesc(desc);
        }else {
            record.setOperatorDesc(operateLog.desc());
        }
        record.setCreateTime(System.currentTimeMillis());
        operateRecordService.add(record);
    }

    private int getResponseCode(HttpServletResponse response) throws IOException {
        if (response.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE) && response instanceof ContentCachingResponseWrapper) {
            String responseStr = IOUtils.toString(((ContentCachingResponseWrapper) response).getContentAsByteArray(), StandardCharsets.UTF_8.name());
            if (StringUtils.isNotBlank(responseStr)) {
                JSONObject jsonObject = JSON.parseObject(responseStr);
                if (jsonObject.getInteger("code") != null) {
                    return jsonObject.getInteger("code");
                }
            }
        }
        return -1;
    }

    private String getDeviceSn(String requestBody) {
        if (StringUtils.isBlank(requestBody)) {
            return null;
        }
        JSONObject jsonObject = JSON.parseObject(requestBody);
        if (StringUtils.isNotBlank(jsonObject.getString("deviceSn"))) {
            return jsonObject.getString("deviceSn");
        }
        if (StringUtils.isNotBlank(jsonObject.getString("sn"))) {
            return jsonObject.getString("sn");
        }
        return null;
    }

    private String getDesc(String requestBody, HandlerMethod methodHandler) {
        if (StringUtils.isBlank(requestBody)) {
            return "";
        }
        MethodParameter[] parameters = methodHandler.getMethodParameters();
        if(parameters.length == 0) {
            return "";
        }
        Class<?> clazz = parameters[0].getParameterType();
        Object jsonObject = JSON.parseObject(requestBody, clazz);
        if(jsonObject instanceof OperateLogInterface) {
            return ((OperateLogInterface)jsonObject).getOperateLog();
        }
        return "";
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        if (request.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE) && request instanceof ContentCachingRequestWrapper) {
            return IOUtils.toString(((ContentCachingRequestWrapper) request).getContentAsByteArray(), StandardCharsets.UTF_8.name());
        }
        return "";
    }

    private String getRemoteIp(HttpServletRequest request) {
        if (request.getHeader("x-forwarded-for") == null) {
            return request.getRemoteAddr();
        }
        return request.getHeader("x-forwarded-for");
    }
}
