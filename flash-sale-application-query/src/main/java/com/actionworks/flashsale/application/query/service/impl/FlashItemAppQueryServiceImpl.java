package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.query.assembler.FlashItemAppAssembler;
import com.actionworks.flashsale.application.query.model.dto.FlashItemDTO;
import com.actionworks.flashsale.application.query.model.req.FlashItemQuery;
import com.actionworks.flashsale.application.query.service.FlashItemAppQueryService;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashItemAppQueryServiceImpl implements FlashItemAppQueryService {
    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public FlashItemDTO getById(Long itemId) {
        FlashItemPO flashItemPO = flashItemMapper.selectById(itemId);
        return FlashItemAppAssembler.toFlashItemDTO(flashItemPO, null);
    }

    @Override
    public List<FlashItemDTO> getFlashItems(FlashItemQuery query) {
        LambdaQueryWrapper<FlashItemPO> queryWrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (query.getStatus() != null) {
                queryWrapper.eq(FlashItemPO::getStatus, query.getStatus());
            }
            if (StringUtils.isNotBlank(query.getItemTitle())) {
                queryWrapper.like(FlashItemPO::getItemTitle, query.getItemTitle());
            }
        }

        List<FlashItemPO> flashItemPOS = flashItemMapper.selectList(queryWrapper);
        return flashItemPOS.stream().map(x -> FlashItemAppAssembler.toFlashItemDTO(x, null)).collect(Collectors.toList());
    }
}
