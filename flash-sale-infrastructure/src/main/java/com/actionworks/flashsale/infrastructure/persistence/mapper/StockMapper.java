package com.actionworks.flashsale.infrastructure.persistence.mapper;

import com.actionworks.flashsale.infrastructure.persistence.model.StockPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<StockPO> {

}
