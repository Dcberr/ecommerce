package com.hcmut.ecommerce.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.domain.user.service.interfaces.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    // @Operation(
    //     summary = "Login with Google",
    //     description = "Exchange Google login data (idToken / authorization code) for an application AuthResponse (access token + user info). This endpoint is public."
    // )
    // @io.swagger.v3.oas.annotations.responses.ApiResponses({
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
    //     @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request")
    // })
    // @PostMapping("/google")
    // public ApiResponse<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) throws Exception {
    //     return ApiResponse.success(authService.loginWithGoogle(request), "Login With Google Successfully!");
    // }

    @Operation(
        summary = "Google OAuth callback",
        description = "Callback endpoint used by Google OAuth flow. It accepts code and role and redirects / sets cookies as implemented by the service."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Callback processed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid callback")
    })
    @GetMapping("/google/callback")
    public void googleCallback(
        @io.swagger.v3.oas.annotations.Parameter(description = "Authorization code returned by Google", required = true) @RequestParam("code") String code,
        @io.swagger.v3.oas.annotations.Parameter(description = "Role to assign to the user (e.g. BUYER or SELLER)", required = true) @RequestParam("role") String role,
        HttpServletResponse response
    ) throws Exception {
        authService.googleCallback(code, role, response);
    }
}