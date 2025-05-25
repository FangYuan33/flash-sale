package com.actionworks.flashsale.domain.model.query;

import lombok.Data;

/**
 * 通用的查询条件
 */
@Data
public class PageQueryCondition {

    /**
     * 每页数量
     */
    protected Integer pageSize;

    /**
     * 页数
     */
    protected Integer pageNum;

}
