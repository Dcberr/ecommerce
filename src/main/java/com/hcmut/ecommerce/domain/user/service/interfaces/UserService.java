package com.hcmut.ecommerce.domain.user.service.interfaces;

import java.util.Set;

import com.hcmut.ecommerce.domain.cart.dto.request.CreateCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.response.CartResponse;

public interface UserService {
  public Set<CartResponse> getCart(String userId);

  public CartResponse addToCart(CreateCartRequest request);

  public void removeFromCart(DeleteCartRequest request);

  public void updateCartAmount(CreateCartRequest request);

  public void clearCart(String userId);
}