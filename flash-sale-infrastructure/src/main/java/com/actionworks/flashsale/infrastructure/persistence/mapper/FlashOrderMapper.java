package com.actionworks.flashsale.infrastructure.persistence.mapper;

import com.actionworks.flashsale.infrastructure.persistence.model.FlashOrderPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FlashOrderMapper extends BaseMapper<FlashOrderPO> {

    @Select("SELECT * FROM flash_order WHERE code = #{code}")
    FlashOrderPO selectByCode(String code);
}
