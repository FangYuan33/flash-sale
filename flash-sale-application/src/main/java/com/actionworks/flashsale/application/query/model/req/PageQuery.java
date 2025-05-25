package com.actionworks.flashsale.application.query.model.req;

import lombok.Data;

@Data
public class PageQuery {
    /**
     * 查询条件 每页展示的个数
     */
    protected Integer pageSize;

    /**
     * 查询条件 展示的页面数
     */
    protected Integer pageNum;
}
