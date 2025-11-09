package com.hcmut.ecommerce.domain.payment.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UpdateEscrowRequest {
    private Integer amount;
    private LocalDateTime releaseAt;
}
