package com.hcmut.ecommerce.domain.order.dto.request;

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
