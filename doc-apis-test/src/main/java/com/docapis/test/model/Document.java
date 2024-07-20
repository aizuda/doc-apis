package com.docapis.test.model;

/**
 * 文档数据模型
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
public class Document {
    /**
     * 主键
     */
    private Long id;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档内容
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
