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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment / MOMO endpoints")
@SecurityRequirement(name = "BearerAuth")
public class MomoPaymentController {
    private final MomoPaymentService momoPaymentService;

    @Operation(
        summary = "Create Momo payment",
        description = "Create a payment request to Momo. Returns payment info (checkout url, request id, ...).",
        tags = {"Payments"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Payment created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    ApiResponse<MomoPaymentResponse> createMomoPayment(@RequestBody MomoPaymentRequest request) throws Exception{
        MomoPaymentResponse response = momoPaymentService.createPaymentRequest(request);
        return ApiResponse.success(response, "Create Momo Payment Successfully!");
    }

    @Operation(
        summary = "Handle Momo callback",
        description = "Endpoint used by Momo to notify payment result. This endpoint is typically public and should be configured in security to allow Momo's callbacks.",
        tags = {"Payments"}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Callback processed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid callback payload")
    })
    @PostMapping("/callback")
    public MomoCallbackResponse handleMomoCallback(@RequestBody MomoCallbackRequest callback) throws Exception {
        log.info("Call back from Momo");
        return momoPaymentService.handleMomoCallback(callback);
    }
}
