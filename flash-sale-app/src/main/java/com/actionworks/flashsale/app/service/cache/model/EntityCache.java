package com.actionworks.flashsale.app.service.cache.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EntityCache<T> {

    /**
     * 该查询条件下的数据是否存在
     */
    private boolean exist;

    /**
     * 实体类数据
     */
    private T data;
}
