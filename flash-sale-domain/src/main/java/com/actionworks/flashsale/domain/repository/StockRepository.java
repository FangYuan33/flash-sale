package com.actionworks.flashsale.domain.repository;

public interface StockRepository {

    int deduct(String itemCode, Integer quantity);

}
