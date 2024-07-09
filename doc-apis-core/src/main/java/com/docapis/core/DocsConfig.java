package com.docapis.core;

import com.docapis.core.constant.CoreConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * abstract doc generator
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class DocsConfig {

    String projectPath; // must set
    List<String> javaSrcPaths = new ArrayList<>(); //multi modules support
    String docsPath; // default equals projectPath
    String resourcePath; // if empty, use the default resources
    String mvcFramework; //spring, play, jfinal, generic, can be empty
    String apiVersion; // this api version
    String projectName; //project name
    Boolean autoGenerate = Boolean.FALSE; // 自动生成所有Controller的接口文档，不需要@DocApi注解
    Locale locale = Locale.getDefault();
    Boolean openReflection = Boolean.TRUE; // 是否开启对象反射

    String rapHost;
    String rapLoginCookie;
    String rapProjectId;
    String rapAccount;
    String rapPassword;

    String watermark = CoreConstants.DEFAULT_WATERMARK;

    String classificationLevel = CoreConstants.DEFAULT_CLASSIFICATION_LEVEL;
    List<IPluginSupport> plugins = new ArrayList<>();

    List<IPluginSupport> getPlugins() {
        return plugins;
    }

    public void addPlugin(IPluginSupport plugin) {
        this.plugins.add(plugin);
    }

    boolean isSpringMvcProject() {
        return mvcFramework != null && mvcFramework.equals("spring");
    }

    boolean isPlayProject() {
        return mvcFramework != null && mvcFramework.equals("play");
    }

    boolean isJfinalProject() {
        return mvcFramework != null && mvcFramework.equals("jfinal");
    }

    boolean isGeneric() {
        return mvcFramework != null && mvcFramework.equals("generic");
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void setDocsPath(String docsPath) {
        this.docsPath = docsPath;
    }

    public void setMvcFramework(String mvcFramework) {
        this.mvcFramework = mvcFramework;
    }

    public List<String> getJavaSrcPaths() {
        return javaSrcPaths;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Boolean getAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(Boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void addJavaSrcPath(String javaSrcPath) {
        javaSrcPaths.add(javaSrcPath);
    }

    public String getRapHost() {
        return rapHost;
    }

    public String getRapLoginCookie() {
        return rapLoginCookie;
    }

    public String getRapProjectId() {
        return rapProjectId;
    }

    public String getRapAccount() {
        return rapAccount;
    }

    public String getRapPassword() {
        return rapPassword;
    }

    public Boolean getOpenReflection() {
        return openReflection;
    }

    public void setOpenReflection(Boolean openReflection) {
        this.openReflection = openReflection;
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
}
