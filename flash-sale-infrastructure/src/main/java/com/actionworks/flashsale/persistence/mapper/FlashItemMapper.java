package com.actionworks.flashsale.persistence.mapper;

import com.actionworks.flashsale.persistence.model.FlashItemDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlashItemMapper {

    void save(FlashItemDO flashItemDO);
}
