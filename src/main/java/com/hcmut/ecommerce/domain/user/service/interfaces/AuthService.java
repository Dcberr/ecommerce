package com.hcmut.ecommerce.domain.user.service.interfaces;

import com.hcmut.ecommerce.domain.user.dto.request.GoogleLoginRequest;
import com.hcmut.ecommerce.domain.user.dto.request.IntrospectRequest;
import com.hcmut.ecommerce.domain.user.dto.response.AuthResponse;
import com.hcmut.ecommerce.domain.user.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.oauth2.sdk.ParseException;

public interface AuthService {
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) throws Exception;
    public String exchangeCodeForIdToken(GoogleLoginRequest request);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
