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
import com.hcmut.ecommerce.domain.user.dto.request.FirstLoginInforRequest;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Users", description = "User endpoints")
@SecurityRequirement(name = "BearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user's cart", description = "Retrieve items in the cart for the specified user", tags = {"Users"})
    @GetMapping("/{id}/cart")
    public ApiResponse<Set<CartResponse>> getCartByUserId(@PathVariable String id) throws Exception {
        return ApiResponse.success(userService.getCart(id), "Get Cart By User Id Successfully!");
    }

    @Operation(summary = "Add item to cart", description = "Add a product to the user's cart", tags = {"Users"})
    @PostMapping("/{id}/cart")
    public ApiResponse<CartResponse> addToCart(@RequestBody CreateCartRequest request) throws Exception {
        return ApiResponse.success(userService.addToCart(request), "Add To Cart Successfully!");
    }

    @Operation(summary = "Update cart item amount", description = "Update the quantity of a product in the user's cart", tags = {"Users"})
    @PutMapping("/{id}/cart")
    public ApiResponse<Void> updateCartAmount(@RequestBody CreateCartRequest request) throws Exception {
        userService.updateCartAmount(request);
        return ApiResponse.success(null, "Update Cart Amount Successfully!");
    }

    @Operation(summary = "Remove item from cart", description = "Remove a specific product from the user's cart", tags = {"Users"})
    @DeleteMapping("/{id}/cart")
    public ApiResponse<Void> removeFromCart(@RequestBody CreateCartRequest request) throws Exception {
        userService.removeFromCart(new com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest(
                request.getBuyerId(), request.getSellerId(), request.getProductId()));
        return ApiResponse.success(null, "Remove From Cart Successfully!");
    }

    @Operation(summary = "Clear cart", description = "Clear all items in the user's cart", tags = {"Users"})
    @DeleteMapping("/{id}/cart/clear")
    public ApiResponse<Void> clearCart(@RequestBody String userId) throws Exception {
        userService.clearCart(userId);
        return ApiResponse.success(null, "Clear Cart Successfully!");
    }

    @Operation(summary = "Get my information", description = "Get profile information for the authenticated user", tags = {"Users"})
    @GetMapping("/my-info")
    public ApiResponse<User> getMyInfor(){
        return ApiResponse.success(userService.getMyInfor());
        
    }

    @Operation(summary = "Update first login information", description = "Update profile data collected during first login", tags = {"Users"})
    @PostMapping("/first-login")
    public ApiResponse<User> updateFirstLoginInfor(@RequestBody FirstLoginInforRequest request){
        return ApiResponse.success(userService.updateFirstLoginInfor(request));
    }
}
