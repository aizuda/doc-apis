package com.docapis.starter.property;

/**
 * 自动跨域配置参数
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
public class CorsConfigProperties {
    /**
     * 是否开启跨域
     */
    private boolean allowCors = true;
    /**
     * 跨域允许时间
     */
    private Long maxAge = 3600L;
    /**
     * 允许跨域请求的域名
     */
    private String pathPattern = "/**";
    /**
     * 允许跨域的来源模式
     */
    private String allowedOriginPatterns = "*";
    /**
     * 允许跨域的方法
     */
    private String allowedMethods = "*";
    /**
     * 跨域允许请求头
     */
    private String allowedHeaders = "*";
    /**
     * 是否允许证书
     */
    private boolean allowCredentials = true;

    public boolean isAllowCors() {
        return allowCors;
    }

    public void setAllowCors(boolean allowCors) {
        this.allowCors = allowCors;
    }

    public Long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Long maxAge) {
        this.maxAge = maxAge;
    }

    public String getPathPattern() {
        return pathPattern;
    }

    public void setPathPattern(String pathPattern) {
        this.pathPattern = pathPattern;
    }

    public String getAllowedOriginPatterns() {
        return allowedOriginPatterns;
    }

    public void setAllowedOriginPatterns(String allowedOriginPatterns) {
        this.allowedOriginPatterns = allowedOriginPatterns;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public boolean isAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
    }
}
