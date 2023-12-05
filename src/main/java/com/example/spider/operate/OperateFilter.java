package com.example.spider.operate;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置request支持直接获取body，response 获取返回结果
 */
@Component
public class OperateFilter extends OncePerRequestFilter {

    // https://segmentfault.com/a/1190000041900931

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // ContentCachingWrapper 缓存请求内容，避免多次获取getInputStream()收到异常IllegalStateException：“getInputStream() has already been called for this request”
        HttpServletRequest requestWrapper = request;
        HttpServletResponse responseWrapper = response;
        if (!(requestWrapper instanceof ContentCachingRequestWrapper)) {
            requestWrapper = new ContentCachingRequestWrapper(request);
        }
        if (!(responseWrapper instanceof ContentCachingResponseWrapper)) {
            responseWrapper = new ContentCachingResponseWrapper(response);
        }
        chain.doFilter(requestWrapper, responseWrapper);
        // 响应结果回写response,否则请求最终返回无结果
        ((ContentCachingResponseWrapper) responseWrapper).copyBodyToResponse();
    }
}

