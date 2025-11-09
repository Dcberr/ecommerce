package com.hcmut.ecommerce.domain.payment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateEscrowRequest {
    private Integer amount;
    private String buyerId;
    private String sellerId;
    private String orderId;
}
