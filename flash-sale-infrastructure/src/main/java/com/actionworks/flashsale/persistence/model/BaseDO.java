package com.actionworks.flashsale.persistence.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseDO {

    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime modifiedTime;
}