package com.hcmut.ecommerce.domain.user.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.cart.dto.request.CreateCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.response.CartResponse;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}/cart")
    public ApiResponse<Set<CartResponse>> getCartByUserId(@PathVariable String id) throws Exception {
        return ApiResponse.success(userService.getCart(id), "Get Cart By User Id Successfully!");
    }

    @PostMapping("/{id}/cart")
    public ApiResponse<CartResponse> addToCart(@RequestBody CreateCartRequest request) throws Exception {
        return ApiResponse.success(userService.addToCart(request), "Add To Cart Successfully!");
    }

    @PutMapping("/{id}/cart")
    public ApiResponse<Void> updateCartAmount(@RequestBody CreateCartRequest request) throws Exception {
        userService.updateCartAmount(request);
        return ApiResponse.success(null, "Update Cart Amount Successfully!");
    }

    @DeleteMapping("/{id}/cart")
    public ApiResponse<Void> removeFromCart(@RequestBody CreateCartRequest request) throws Exception {
        userService.removeFromCart(new com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest(
                request.getBuyerId(), request.getSellerId(), request.getProductId()));
        return ApiResponse.success(null, "Remove From Cart Successfully!");
    }

    @DeleteMapping("/{id}/cart/clear")
    public ApiResponse<Void> clearCart(@RequestBody String userId) throws Exception {
        userService.clearCart(userId);
        return ApiResponse.success(null, "Clear Cart Successfully!");
    }
}
