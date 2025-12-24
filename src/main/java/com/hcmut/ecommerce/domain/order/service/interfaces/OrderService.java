package com.hcmut.ecommerce.domain.order.service.interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.model.Order;

public interface OrderService {
    public List<Order> getAllOrders();
    public Order getOrderById(String id);
    public Order createOrder(CreateOrderRequest request) throws JsonMappingException, JsonProcessingException;
    public Order updateOrder(String id, Integer totalAmount);
    public void deleteOrder(String id);
    public List<Order> getOrderBySellerId();
    public List<Order> getOrderByBuyerId();
    public void confirmOrder(String id);
    public void completeOrder(String id);
    public void cancelOrder(String id);
}
