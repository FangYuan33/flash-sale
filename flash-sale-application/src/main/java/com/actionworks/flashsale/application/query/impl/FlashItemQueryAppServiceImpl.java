package com.actionworks.flashsale.application.query.impl;

import com.actionworks.flashsale.application.ability.CacheService;
import com.actionworks.flashsale.application.query.convertor.FlashItemAppConvertor;
import com.actionworks.flashsale.application.model.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.model.FlashItemQuery;
import com.actionworks.flashsale.application.model.result.AppResult;
import com.actionworks.flashsale.application.query.FlashItemQueryAppService;
import com.actionworks.flashsale.common.constants.CacheConstants;
import com.actionworks.flashsale.domain.model.item.aggregate.FlashItem;
import com.actionworks.flashsale.domain.model.query.FlashItemQueryCondition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashItemQueryAppServiceImpl implements FlashItemQueryAppService {

    @Resource
    private CacheService<FlashItem> cacheService;

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<FlashItemDTO> getById(Long itemId) {
        FlashItem flashItem = cacheService.getCache(CacheConstants.FLASH_ITEM_SINGLE_CACHE_PREFIX, itemId);

        // 库存缓存已预热的话，则以库存缓存为准，否则取的仍然是商品缓存中的库存值
        Integer availableItemStock = itemStockCacheService.getAvailableItemStock(itemId);
        if (!availableItemStock.equals(-1)) {
            flashItem.setAvailableStock(availableItemStock);
        }

        FlashItemDTO flashItemDTO = FlashItemAppConvertor.toFlashItemDTO(flashItem);

        return AppResult.success(flashItemDTO);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppResult<List<FlashItemDTO>> getFlashItems(FlashItemQuery query) {
        FlashItemQueryCondition queryCondition = FlashItemAppConvertor.toFlashItemQueryCondition(query);

        List<FlashItem> flashItemEntities = cacheService.getCaches(CacheConstants.FLASH_ITEM_CACHE_LIST_PREFIX, queryCondition);

        // stream 转换对象类型
        List<FlashItemDTO> itemDTOS = flashItemEntities.stream()
                .map(FlashItemAppConvertor::toFlashItemDTO).collect(Collectors.toList());

        return AppResult.success(itemDTOS);
    }

}
