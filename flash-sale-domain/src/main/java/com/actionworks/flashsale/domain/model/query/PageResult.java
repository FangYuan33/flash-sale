package com.actionworks.flashsale.domain.model.query;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批量分页查询返回的结果对象
 */
@Data
@Accessors(chain = true)
public class PageResult<T> {
    private List<T> data;
    private int total;

    private PageResult(int total, List<T> data) {
        this.setData(data);
        this.total = total;
    }

    public static <T> PageResult<T> with(List<T> data, int total) {
        return new PageResult<>(total, data);
    }
}