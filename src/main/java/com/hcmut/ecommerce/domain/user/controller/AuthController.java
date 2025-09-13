package com.hcmut.ecommerce.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.user.dto.request.GoogleLoginRequest;
import com.hcmut.ecommerce.domain.user.dto.response.AuthResponse;
import com.hcmut.ecommerce.domain.user.service.interfaces.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ApiResponse<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) throws Exception {
        return ApiResponse.success(authService.loginWithGoogle(request), "Login With Google Successfully!");
    }
}
