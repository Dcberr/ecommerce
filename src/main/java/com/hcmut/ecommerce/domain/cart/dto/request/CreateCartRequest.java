package com.hcmut.ecommerce.domain.cart.dto.request;

import com.hcmut.ecommerce.domain.cart.model.Cart;
import com.hcmut.ecommerce.domain.productListing.model.ProductListing.ProductListingId;
import com.hcmut.ecommerce.domain.cart.model.Cart.CartId;

import io.micrometer.common.lang.NonNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCartRequest {
  @NonNull
  private String buyerId;
  @NonNull
  private String sellerId;
  @NonNull
  private Long productId;
  @NonNull
  private Long amount;

  public Cart toCart() {
    return new Cart(new CartId(buyerId, new ProductListingId(sellerId, productId)), null, null, amount);
  }
}