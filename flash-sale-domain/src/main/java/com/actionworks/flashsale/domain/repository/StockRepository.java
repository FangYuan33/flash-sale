package com.actionworks.flashsale.domain.repository;

public interface StockRepository {

    boolean deduct(String itemCode, Integer quantity);

}
