package com.hcmut.ecommerce.domain.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MomoCallbackResponse {
    private String message;
    private int resultCode;
}

