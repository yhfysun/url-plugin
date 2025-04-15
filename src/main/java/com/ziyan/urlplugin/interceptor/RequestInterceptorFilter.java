package com.ziyan.urlplugin.interceptor;

import com.ziyan.urlplugin.entity.NodeUrlAndApiRelation;
import com.ziyan.urlplugin.mapper.NodeUrlAndApiRelationMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebFilter(urlPatterns = "/*")  // 拦截所有请求
@Slf4j
public class RequestInterceptorFilter extends OncePerRequestFilter {

    @Value("${spring.application.name}")
    private String serverName;  // 从配置文件获取服务名称

    @Resource
    private NodeUrlAndApiRelationMapper apiRelationMapper;
    
    @Resource
    private RequestMappingHandlerMapping handlerMapping;  // 获取处理方法


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取请求头中的 node-url 参数
            String nodeUrl = request.getHeader("node-url");
            log.info("进入url拦截组件：nodeUrl: {}，", nodeUrl);
            
            // 打印所有请求头
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                log.info("请求头 - {}: {}", headerName, headerValue);
            }
            
            // 获取请求的 HandlerMethod
            HandlerMethod handler = (HandlerMethod) handlerMapping.getHandler(request).getHandler();
            if (nodeUrl != null) {
                // 获取方法上的@ApiOperation注解
                ApiOperation apiOperation = handler.getMethodAnnotation(ApiOperation.class);
                if (apiOperation != null) {
                    String url = request.getRequestURI();
                    String methodDescription = apiOperation.value();
    
                    // 保存到数据库
                    saveUrlToDatabase(nodeUrl, url, methodDescription);
                }
            }
        } catch (Exception e) {
            log.error("保存url失败");
            log.error("e: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    private void saveUrlToDatabase(String nodeUrl, String api, String methodDescription) {
        NodeUrlAndApiRelation entity = new NodeUrlAndApiRelation();
        entity.setNodeUrl(nodeUrl);  // 前端请求头中的 node_url
        entity.setApi(api);          // 后端 API URL
        entity.setDescription(methodDescription);  // 方法的描述
        entity.setServerName(serverName);  // 服务名称

        // 将数据保存到数据库，确保唯一性
        try {
            apiRelationMapper.insert(entity);
        } catch (Exception e) {
        }
    }
}
