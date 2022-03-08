package com.actionworks.flashsale.persistence.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDO {

    @TableId(value = "id", type = IdType.AUTO)
    protected Long id;

    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime modifiedTime;
}