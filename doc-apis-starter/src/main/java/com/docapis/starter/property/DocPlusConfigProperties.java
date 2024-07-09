package com.docapis.starter.property;

import com.docapis.core.constant.CoreConstants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Locale;

/**
 * 配置
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
@ConfigurationProperties(value = "doc-apis")
@ConditionalOnProperty(prefix = "doc-apis", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class DocPlusConfigProperties {
    /**
     * 是否开启文档自动生成 默认开启
     */
    private boolean enable = true;
    /**
     * 项目名称
     */
    private String projectName = "doc-apis";
    /**
     * 项目路径
     */
    private String projectPath = System.getProperty(CoreConstants.USER_DIR);
    /**
     * 文档版本
     */
    private String docVersion = "doc-version-" + CoreConstants.VERSION;
    /**
     * 文档生成路径
     */
    private String docPath = System.getProperty(CoreConstants.USER_DIR);
    /**
     * 是否自动生成 默认为true 若为false时,需要加@DocApi注解
     */
    private boolean autoGenerate = true;
    /**
     * 是否生成markdown文档,默认生成,若为否则仅生成html文档
     */
    private boolean generateMarkDown = true;
    /**
     * 水印
     */
    private String watermark;
    /**
     * 接口文档密级
     */
    private String classificationLevel;

    /**
     * 跨域相关配置
     */
    @NestedConfigurationProperty
    private CorsConfigProperties corsConfig = new CorsConfigProperties();

    /**
     * 生成文档语言,默认为当前系统语言
     */
    private Locale locale = Locale.getDefault();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getDocVersion() {
        return docVersion;
    }

    public void setDocVersion(String docVersion) {
        this.docVersion = docVersion;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public boolean isGenerateMarkDown() {
        return generateMarkDown;
    }

    public void setGenerateMarkDown(boolean generateMarkDown) {
        this.generateMarkDown = generateMarkDown;
    }

    public CorsConfigProperties getCorsConfig() {
        return corsConfig;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getWatermark() {
        return watermark;
    }

    public void setWatermark(String watermark) {
        this.watermark = watermark;
    }

    public String getClassificationLevel() {
        return classificationLevel;
    }

    public void setClassificationLevel(String classificationLevel) {
        this.classificationLevel = classificationLevel;
    }

    public void setCorsConfig(CorsConfigProperties corsConfig) {
        this.corsConfig = corsConfig;
    }
}
