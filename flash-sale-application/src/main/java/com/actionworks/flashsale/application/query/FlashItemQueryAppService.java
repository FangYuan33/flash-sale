package com.actionworks.flashsale.application.query;

import com.actionworks.flashsale.application.query.model.FlashItemQuery;
import com.actionworks.flashsale.application.model.result.AppResult;

public interface FlashItemQueryAppService {

    /**
     * 通过ID 获取单条秒杀商品信息
     *
     * @param itemId 秒杀商品ID
     */
    <T> AppResult<T> getById(Long itemId);

    /**
     * 条件查询秒杀商品
     */
    <T> AppResult<T> getFlashItems(FlashItemQuery query);

}
