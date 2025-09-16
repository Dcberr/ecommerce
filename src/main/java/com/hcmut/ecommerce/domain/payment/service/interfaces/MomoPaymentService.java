package com.hcmut.ecommerce.domain.payment.service.interfaces;

import java.util.Map;

import com.hcmut.ecommerce.domain.payment.dto.request.MomoCallbackRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.MomoPaymentRequest;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoCallbackResponse;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoPaymentResponse;

public interface MomoPaymentService {

    public String generateHmacSHA256(Map<String, String> params, String secretKey) throws Exception;

    public MomoPaymentResponse createPaymentRequest(MomoPaymentRequest request) throws Exception;

    public MomoCallbackResponse handleMomoCallback(MomoCallbackRequest callback) throws Exception;
}
