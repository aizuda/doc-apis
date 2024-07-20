package com.docapis.core.constant;


import java.io.File;

/**
 * 核心常量
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
public interface CoreConstants {
    /**
     * 版本号发版及包调整时须更新此字段,死贫道不能死道友,此举可使用户可免配
     */
    String VERSION = "1.0.0";
    /**
     * jar 完整路径
     */
    String JAR_PATH = "com" + File.separator + "doc-apis" + File.separator + "doc-apis-core" + File.separator + VERSION + File.separator + "doc-apis-core-" + VERSION + ".jar";
    /**
     * 分隔符
     */
    String SEPATOR = "/";
    /**
     * comma
     */
    String COMMA_SIGN = ",";
    /**
     * 当前项目目录
     */
    String USER_DIR = "user.dir";
    /**
     * js存放路径
     */
    String JS_PATH = "js";
    /**
     * 图片存放路径
     */
    String IMG_PATH = "img";
    /**
     * css存放路径
     */
    String CSS_PATH = "css";
    /**
     * 字体存放路径
     */
    String FONT_PATH = "fonts";
    /**
     * html存放路径
     */
    String HTML_PATH = "html";


    String LOGO_NAME = "logo.png";
    /**
     * pageInfo 类名
     */
    String PAGE_INFO_NAME = "PageInfo";
    /**
     * pageInfo 文件名
     */
    String PAGE_INFO_FILE = "PageInfo.java";
    /**
     * esPageInfo 类名
     */
    String ES_PAGE_INFO_NAME = "EsPageInfo";
    /**
     * esPageInfo 文件名
     */
    String ES_PAGE_INFO_FILE = "EsPageInfo.java";
    /**
     * java 文件后缀
     */
    String JAVA_FILE_SUFFIX = ".java";
    /**
     * 内部依赖的所有js列表
     */
    String JS_NAMES = "jquery.min.js,bootstrap.min.js,autocomplete.jquery.min.js,prettify.min.js";
    /**
     * 内部依赖的所有css列表
     */
    String CSS_NAMES = "style.css,bootstrap.min.css,font-awesome.min.css,prettify.min.css";
    /**
     * 内部依赖的所有字体列表
     */
    String FONT_NAMES = "fontawesome-webfont.ttf,glyphicons-halflings-regular.ttf";
    /**
     * 缓存文件
     */
    String CACHE_FILE = ".cache.json";
    /**
     * 默认水印
     */
    String DEFAULT_WATERMARK = "doc-apis";
    /**
     * 默认密级
     */
    String DEFAULT_CLASSIFICATION_LEVEL = "II";
}
