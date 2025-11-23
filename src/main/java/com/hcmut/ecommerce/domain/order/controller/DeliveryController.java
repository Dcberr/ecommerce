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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Delivery", description = "Delivery & shipping endpoints")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Create GHTK order", description = "Create an order in GHTK (third-party delivery). Returns created order data and message.", tags = {"Delivery"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "GHTK order created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ApiResponse<CreateGhtkOrderResponse.Order> createGhtkOrder(@RequestBody CreateGhtkOrderRequest request) throws JsonMappingException, JsonProcessingException{
        CreateGhtkOrderResponse response = deliveryService.createOrder(request);
        return ApiResponse.success(response.getOrder(), response.getMessage());
    }

    @Operation(summary = "Get shipping fee", description = "Compute shipping fee for provided parameters. Note: this endpoint expects a request body with fee details.", tags = {"Delivery"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Shipping fee returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/fee")
    public ApiResponse<FeeResponse> getFee(
        @RequestBody FeeRequest request
    ) throws JsonMappingException, JsonProcessingException{
        return ApiResponse.success(deliveryService.getShippingFee(request));
    }
}
