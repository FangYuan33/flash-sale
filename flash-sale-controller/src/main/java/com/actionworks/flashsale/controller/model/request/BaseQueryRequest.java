package com.actionworks.flashsale.controller.model.request;

import lombok.Data;

@Data
public class BaseQueryRequest {
    /**
     * 查询条件 每页展示的个数
     */
    protected Integer pageSize;

    /**
     * 查询条件 展示的页面数
     */
    protected Integer pageNum;
}
