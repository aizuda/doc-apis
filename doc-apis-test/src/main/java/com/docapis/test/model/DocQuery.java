package com.docapis.test.model;

import javax.validation.constraints.NotNull;

/**
 * 文档查询参数
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/

public class DocQuery {
    /**
     * 标题
     */
    private String title;
    /**
     * 页码
     */
    @NotNull
    private Integer pageNum;
    /**
     * 每页条数
     */
    @NotNull
    private Integer pageSize;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
