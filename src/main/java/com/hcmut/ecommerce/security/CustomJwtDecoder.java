package com.hcmut.ecommerce.security;

import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.hcmut.ecommerce.domain.user.dto.request.IntrospectRequest;
import com.hcmut.ecommerce.domain.user.dto.response.IntrospectResponse;
import com.hcmut.ecommerce.domain.user.service.interfaces.AuthService;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private AuthService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) {
        try {
            IntrospectResponse response = authenticationService
                    .introspect(IntrospectRequest.builder().token(token).build());

            if (!(response.getValid())) {
                throw new Exception("Invalidated Token!!!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(clientSecret.getBytes(), "HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }

}
