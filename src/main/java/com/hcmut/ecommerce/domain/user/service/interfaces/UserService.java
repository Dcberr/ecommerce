package com.hcmut.ecommerce.domain.user.service.interfaces;

import java.util.Set;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.hcmut.ecommerce.domain.cart.dto.request.CreateCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.request.DeleteCartRequest;
import com.hcmut.ecommerce.domain.cart.dto.response.CartResponse;
import com.hcmut.ecommerce.domain.user.model.Buyer;
import com.hcmut.ecommerce.domain.user.model.Seller;

public interface UserService {
  public Set<CartResponse> getCart(String userId);

  public CartResponse addToCart(CreateCartRequest request);

  public void removeFromCart(DeleteCartRequest request);

  public void updateCartAmount(CreateCartRequest request);

  public void clearCart(String userId);

  public Buyer buyerLogin(GoogleIdToken.Payload payload);

  public Seller sellerLogin(GoogleIdToken.Payload payload);
}