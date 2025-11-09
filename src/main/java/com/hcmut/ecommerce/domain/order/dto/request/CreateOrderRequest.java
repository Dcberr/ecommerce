package com.hcmut.ecommerce.domain.order.dto.request;

import com.hcmut.ecommerce.domain.user.model.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderRequest {
    private Integer totalProductPrice;
    private String buyerId;
    private String sellerId;
    private String note;
    // private Long amount;
}
