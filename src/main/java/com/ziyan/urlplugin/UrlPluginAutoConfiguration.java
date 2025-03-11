package com.ziyan.urlplugin;

import com.ziyan.urlplugin.interceptor.RequestInterceptorFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages  = "com.ziyan.urlplugin.mapper")
@EntityScan(basePackages  = "com.ziyan.urlplugin.entity")
public class UrlPluginAutoConfiguration {

    @Bean
    public RequestInterceptorFilter requestInterceptorFilter() {
        return new RequestInterceptorFilter();
    }
}
