package com.hcmut.ecommerce.domain.cart.dto.response;

import com.hcmut.ecommerce.domain.cart.model.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CartResponse {
  private String buyerId;
  private String sellerId;
  private Long productId;
  private Long amount;
  private String sellerName;

  public CartResponse(Cart cart) {
    this.buyerId = cart.getBuyer().getId();
    this.sellerId = cart.getProduct().getSellerId();
    this.productId = cart.getProduct().getId();
    this.amount = cart.getAmount();
    // this.sellerName = cart.getProduct().getSeller().getName();
  }
}