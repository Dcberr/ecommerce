package com.hcmut.ecommerce.domain.cart.dto.request;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCartRequest {
  @NonNull
  private String buyerId;
  @NonNull
  private String sellerId;
  @NonNull
  private Long productId;
}