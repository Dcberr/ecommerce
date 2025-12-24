package com.hcmut.ecommerce.domain.cart.dto.request;

import com.hcmut.ecommerce.domain.cart.model.Cart;
import com.hcmut.ecommerce.domain.cart.model.Cart.CartId;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartRequest {
  private String buyerId;
  @NonNull
  private Long productId;
  @NonNull
  private Long amount;

  public Cart toCart() {
    return new Cart(new CartId(buyerId, productId), null, null, amount);
  }
}