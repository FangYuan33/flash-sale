package com.actionworks.flashsale.application.query.service;

import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;

import java.util.List;

public interface FlashItemAppQueryService {

    /**
     * 通过ID 获取单条秒杀商品信息
     *
     * @param itemId 秒杀商品ID
     */
    FlashItemDTO getById(Long itemId);

    List<FlashItemDTO> getFlashItems(FlashItemQuery query);
}
