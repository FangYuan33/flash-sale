package com.actionworks.flashsale.infrastructure.cache.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class EntityCache<T> implements Serializable {

    /**
     * 该查询条件下的数据是否存在
     */
    private boolean exist;

    /**
     * 实体类数据列表，单个值和多个值都用这个存
     */
    private List<T> dataList;
}
