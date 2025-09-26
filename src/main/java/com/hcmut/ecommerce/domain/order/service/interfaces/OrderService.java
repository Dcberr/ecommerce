package com.hcmut.ecommerce.domain.order.service.interfaces;

import java.util.List;

import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.model.Order;

public interface OrderService {
    public List<Order> getAllOrders();
    public Order getOrderById(String id);
    public Order createOrder(CreateOrderRequest request);
    public Order updateOrder(String id, Float totalAmount);
    public void deleteOrder(String id);
}
