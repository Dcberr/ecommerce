package com.hcmut.ecommerce.domain.order.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateOrderRequest {

    // private String sellerId;
    private List<OrderItemRequest> items;
    private String note;

    @Getter @Setter
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}
