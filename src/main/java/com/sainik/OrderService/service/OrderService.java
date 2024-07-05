package com.sainik.OrderService.service;

import com.sainik.OrderService.model.OrderRequest;
import com.sainik.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
