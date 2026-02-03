package com.ecommerce.order.service;

import com.ecommerce.order.entity.Order;

public interface OrderService {
    Order placeOrder(Long productId, Integer quantity);
}
