package com.hcmut.ecommerce.domain.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.service.interfaces.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/order")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        return ApiResponse.success(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable String id){
        return ApiResponse.success(orderService.getOrderById(id));
    }

    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody CreateOrderRequest request) throws JsonMappingException, JsonProcessingException{
        return ApiResponse.success(orderService.createOrder(request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable String id){
        orderService.deleteOrder(id);
        return ApiResponse.success(null);
    }
    
    @GetMapping("/seller")
    public ApiResponse<List<Order>> getOrderBySellerId(){
        return ApiResponse.success(orderService.getOrderBySellerId());
    }
}
