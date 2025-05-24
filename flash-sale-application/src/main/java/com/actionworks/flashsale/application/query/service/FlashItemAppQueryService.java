package com.actionworks.flashsale.application.query.service;

import com.actionworks.flashsale.application.query.model.item.dto.FlashItemDTO;

public interface FlashItemAppQueryService {

    /**
     * 通过ID 获取单条秒杀商品信息
     *
     * @param itemId 秒杀商品ID
     */
    FlashItemDTO getById(Long itemId);

}
