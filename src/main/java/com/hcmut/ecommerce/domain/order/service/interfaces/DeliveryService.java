package com.hcmut.ecommerce.domain.order.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.domain.order.dto.request.CreateGhtkOrderRequest;
import com.hcmut.ecommerce.domain.order.dto.request.FeeRequest;
import com.hcmut.ecommerce.domain.order.dto.response.CreateGhtkOrderResponse;
import com.hcmut.ecommerce.domain.order.dto.response.FeeResponse;

public interface DeliveryService {
    public CreateGhtkOrderResponse createOrder(CreateGhtkOrderRequest orderRequest) throws JsonMappingException, JsonProcessingException;
    public String getOrder(String labelId);
    public String cancelOrder(String labelId);
    public FeeResponse getShippingFee(FeeRequest params) throws JsonMappingException, JsonProcessingException;
    public String trackOrder(String labelId);
}
