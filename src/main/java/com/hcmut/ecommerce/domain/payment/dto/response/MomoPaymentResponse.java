package com.hcmut.ecommerce.domain.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // Add this annotation
public class MomoPaymentResponse {
    private String partnerCode;
    private int resultCode;
    private String message;
    private String orderId;
    private String requestId;
    private String signature;
    private String amount;
    private String payUrl;
    private String qrCodeUrl;
    private String deeplinkMiniApp;
    private String responseTime;
}
