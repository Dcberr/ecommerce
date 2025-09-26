package com.hcmut.ecommerce.domain.order.dto.request;

import com.hcmut.ecommerce.domain.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderRequest {
    private Float totalAmount;
    private String buyerId;
    private String sellerId;
    // private Long amount;
}
