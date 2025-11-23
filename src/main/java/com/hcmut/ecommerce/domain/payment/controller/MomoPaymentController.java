package com.hcmut.ecommerce.domain.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.payment.dto.request.MomoCallbackRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.MomoPaymentRequest;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoCallbackResponse;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoPaymentResponse;
import com.hcmut.ecommerce.domain.payment.service.interfaces.MomoPaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class MomoPaymentController {
    private final MomoPaymentService momoPaymentService;

    @PostMapping
    ApiResponse<MomoPaymentResponse> createMomoPayment(@RequestBody MomoPaymentRequest request) throws Exception{
        MomoPaymentResponse response = momoPaymentService.createPaymentRequest(request);
        return ApiResponse.success(response, "Create Momo Payment Successfully!");
    }

    @PostMapping("/callback")
    public MomoCallbackResponse handleMomoCallback(@RequestBody MomoCallbackRequest callback) throws Exception {
        log.info("Call back form Momo");
        return momoPaymentService.handleMomoCallback(callback);
    }
}
