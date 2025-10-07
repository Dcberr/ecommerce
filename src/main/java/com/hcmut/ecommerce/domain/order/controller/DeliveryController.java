package com.hcmut.ecommerce.domain.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.order.dto.request.CreateGhtkOrderRequest;
import com.hcmut.ecommerce.domain.order.dto.request.FeeRequest;
import com.hcmut.ecommerce.domain.order.dto.response.CreateGhtkOrderResponse;
import com.hcmut.ecommerce.domain.order.dto.response.FeeResponse;
import com.hcmut.ecommerce.domain.order.service.interfaces.DeliveryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ApiResponse<CreateGhtkOrderResponse.Order> createGhtkOrder(@RequestBody CreateGhtkOrderRequest request) throws JsonMappingException, JsonProcessingException{
        CreateGhtkOrderResponse response = deliveryService.createOrder(request);
        return ApiResponse.success(response.getOrder(), response.getMessage());
    }

    @GetMapping("/fee")
    public ApiResponse<FeeResponse> getFee(
        @RequestBody FeeRequest request
    ) throws JsonMappingException, JsonProcessingException{
        return ApiResponse.success(deliveryService.getShippingFee(request));
    }
}
