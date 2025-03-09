package com.ziyan.urlplugin.config;

import com.ziyan.urlplugin.interceptor.SwaggerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SwaggerInterceptor swaggerInterceptor;

    public WebConfig(SwaggerInterceptor swaggerInterceptor) {
        this.swaggerInterceptor = swaggerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(swaggerInterceptor)
                .addPathPatterns("/**"); // 拦截所有请求
    }
}
