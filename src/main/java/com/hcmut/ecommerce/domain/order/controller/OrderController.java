package com.hcmut.ecommerce.domain.order.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.service.interfaces.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/order")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Orders", description = "Order lifecycle")
public class OrderController {
    private final OrderService orderService;

    @Operation(
        summary = "List all orders",
        description = "Retrieve a list of all orders. Admins or users with proper roles only.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of orders returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ApiResponse<List<Order>> getAllOrders() {
        return ApiResponse.success(orderService.getAllOrders());
    }

    @Operation(
        summary = "Get order by id",
        description = "Retrieve details for a specific order by its id.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(@PathVariable String id){
        return ApiResponse.success(orderService.getOrderById(id));
    }

    @Operation(
        summary = "Create order",
        description = "Create a new order from the provided payload.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ApiResponse<Order> createOrder(@RequestBody CreateOrderRequest request) throws JsonMappingException, JsonProcessingException{
        return ApiResponse.success(orderService.createOrder(request));
    }

    @Operation(
        summary = "Delete order",
        description = "Delete an order by id. Only allowed for authorized users.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Order deleted"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Order not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrder(@PathVariable String id){
        orderService.deleteOrder(id);
        return ApiResponse.success(null);
    }
    
    @Operation(
        summary = "Get orders by seller",
        description = "Retrieve orders associated with the authenticated seller.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders returned for seller"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/seller")
    public ApiResponse<List<Order>> getOrderBySellerId(){
        return ApiResponse.success(orderService.getOrderBySellerId());
    }

    @Operation(
        summary = "Get orders by buyer",
        description = "Retrieve orders associated with the authenticated buyer.",
        tags = {"Orders"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Orders returned for buyer"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/buyer")
    public ApiResponse<List<Order>> getOrderByBuyerId(){
        return ApiResponse.success(orderService.getOrderByBuyerId());
    }

    @PutMapping({"/confirm/{id}"})
    public ApiResponse<Void> confirmOrder(@PathVariable String id) {
        orderService.confirmOrder(id);
        return ApiResponse.success(null);
    }

    @PutMapping({"/complete/{id}"})
    public ApiResponse<Void> completeOrder(@PathVariable String id) {
        orderService.completeOrder(id);
        return ApiResponse.success(null);
    }

    @PutMapping({"/cancel/{id}"})
    public ApiResponse<Void> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ApiResponse.success(null);
    }
}