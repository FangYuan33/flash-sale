package com.actionworks.flashsale.application.query.service.impl;

import com.actionworks.flashsale.application.query.assembler.FlashActivityAppAssembler;
import com.actionworks.flashsale.application.query.assembler.FlashItemAppAssembler;
import com.actionworks.flashsale.application.query.model.dto.FlashActivityDTO;
import com.actionworks.flashsale.application.query.service.FlashActivityAppQueryService;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashActivityMapper;
import com.actionworks.flashsale.infrastructure.persistence.mapper.FlashItemMapper;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashActivityPO;
import com.actionworks.flashsale.infrastructure.persistence.model.FlashItemPO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class FlashActivityAppQueryServiceImpl implements FlashActivityAppQueryService {

    @Resource
    private FlashActivityMapper flashActivityMapper;

    @Resource
    private FlashItemMapper flashItemMapper;

    @Override
    public FlashActivityDTO findByCode(String code) {
        LambdaQueryWrapper<FlashActivityPO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlashActivityPO::getCode, code);
        FlashActivityPO flashActivityPO = flashActivityMapper.selectOne(queryWrapper);
        if (flashActivityPO == null) {
            return null;
        }
        FlashActivityDTO flashActivityDTO = FlashActivityAppAssembler.toDTO(flashActivityPO);
        LambdaQueryWrapper<FlashItemPO> itemQueryWrapper = new LambdaQueryWrapper<>();
        itemQueryWrapper.eq(FlashItemPO::getCode, flashActivityPO.getItemCode());
        FlashItemPO flashItemPO = flashItemMapper.selectOne(itemQueryWrapper);
        if (flashItemPO != null) {
            flashActivityDTO.setFlashItem(FlashItemAppAssembler.toFlashItemDTO(flashItemPO, null));
        }
        return flashActivityDTO;
    }
}
