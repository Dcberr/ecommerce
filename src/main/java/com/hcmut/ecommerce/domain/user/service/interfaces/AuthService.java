package com.hcmut.ecommerce.domain.user.service.interfaces;

import com.hcmut.ecommerce.domain.user.dto.request.GoogleLoginRequest;
import com.hcmut.ecommerce.domain.user.dto.response.AuthResponse;

public interface AuthService {
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) throws Exception;
    public String exchangeCodeForIdToken(GoogleLoginRequest request);
}
