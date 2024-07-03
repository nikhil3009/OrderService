package com.sainik.OrderService.service;

import com.sainik.OrderService.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
