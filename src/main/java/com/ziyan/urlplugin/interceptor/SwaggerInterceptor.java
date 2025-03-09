package com.ziyan.urlplugin.interceptor;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;
import com.ziyan.urlplugin.repository.NodeUrlAndApiRelationRepository;
import com.ziyan.urlplugin.entity.NodeUrlAndApiRelation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SwaggerInterceptor implements HandlerInterceptor {

    @Value("${service.name}")
    private String serverName;  // 从配置文件获取服务名称

    private final NodeUrlAndApiRelationRepository repository;

    public SwaggerInterceptor(NodeUrlAndApiRelationRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 获取请求头中的 node_url 参数
            String nodeUrl = request.getHeader("node_url");
            if (nodeUrl != null) {
                // 获取方法上的@ApiOperation注解
                ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
                if (apiOperation != null) {
                    String url = request.getRequestURI();
                    String methodDescription = apiOperation.value();

                    // 保存到数据库
                    saveUrlToDatabase(nodeUrl, url, methodDescription);
                }
            }
        }
        return true;
    }

    private void saveUrlToDatabase(String nodeUrl, String api, String methodDescription) {
        NodeUrlAndApiRelation entity = new NodeUrlAndApiRelation();
        entity.setNodeUrl(nodeUrl);  // 前端请求头中的 node_url
        entity.setApi(api);          // 后端 API URL
        entity.setDescription(methodDescription);  // 方法的描述
        entity.setServerName(serverName);  // 服务名称

        // 将数据保存到数据库，确保唯一性
        try {
            repository.save(entity);
        } catch (Exception e) {
            // 捕获异常，确保数据库唯一性
            System.out.println("Duplicate entry detected for nodeUrl: " + nodeUrl + " and api: " + api);
        }
    }
}
