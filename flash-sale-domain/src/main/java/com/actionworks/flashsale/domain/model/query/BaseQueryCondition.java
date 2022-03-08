package com.actionworks.flashsale.domain.model.query;

import lombok.Data;

/**
 * 通用的查询条件
 */
@Data
public class BaseQueryCondition {

    /**
     * 默认每页数量
     */
    private static final Integer NORMAL_PAGE_SIZE = 10;

    /**
     * 最大每页数量
     */
    private static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 默认页数
     */
    private static final Integer NORMAL_PAGE_NUM = 1;

    /**
     * 最大页数
     */
    private static final Integer MAX_PAGE_NUM = 10;

    /**
     * 每页数量
     */
    protected Integer pageSize;

    /**
     * 页数
     */
    protected Integer pageNum;

    /**
     * 分页偏移量
     */
    protected Integer offset;

    /**
     * 校正pageSize和pageNum分页参数
     */
    protected void buildParams() {
        pageSize = pageSize == null ? NORMAL_PAGE_SIZE : pageSize < 0 ? -pageSize : pageSize;
        pageSize = pageSize > MAX_PAGE_SIZE ? MAX_PAGE_SIZE : pageSize;

        pageNum = pageNum == null ? NORMAL_PAGE_NUM : pageNum < 0 ? -pageNum : pageNum;
        pageNum = pageNum > MAX_PAGE_NUM ? MAX_PAGE_NUM : pageNum;

        offset = (pageNum - 1) * pageSize;
    }
}
