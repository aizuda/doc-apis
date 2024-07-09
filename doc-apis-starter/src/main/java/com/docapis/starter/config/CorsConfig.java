package com.docapis.starter.config;

import com.docapis.starter.property.CorsConfigProperties;
import com.docapis.starter.property.DocPlusConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static com.docapis.core.constant.CoreConstants.COMMA_SIGN;

/**
 * 跨域自动配置
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
@Configuration
@EnableWebMvc
@ConditionalOnProperty(prefix = "doc-apis.corsConfig", name = {"allowCors"}, havingValue = "true", matchIfMissing = true)
public class CorsConfig implements WebMvcConfigurer {
    @Autowired
    private DocPlusConfigProperties docPlusConfigProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsConfigProperties config = docPlusConfigProperties.getCorsConfig();
        String[] allowedOriginPatterns = config.getAllowedOriginPatterns().split(COMMA_SIGN);
        String[] allowedMethods = config.getAllowedMethods().split(COMMA_SIGN);
        String[] allowedHeaders = config.getAllowedHeaders().split(COMMA_SIGN);
        registry.addMapping(config.getPathPattern())
                // 设置允许跨域请求的域名
                .allowedOriginPatterns(allowedOriginPatterns)
                // 设置允许的方法
                .allowedMethods(allowedMethods)
                // 允许的请求头
                .allowedHeaders(allowedHeaders)
                // 跨域允许时间
                .maxAge(config.getMaxAge())
                // 是否允许证书 不在默认开启
                .allowCredentials(config.isAllowCredentials());
    }
}
