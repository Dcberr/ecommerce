
package com.hcmut.ecommerce.domain.user.service.implement;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.hcmut.ecommerce.domain.user.service.interfaces.GoogleTokenVerifierService;

@Service
public class GoogleTokenVerifierServiceImpl implements GoogleTokenVerifierService {

    private final String clientId;

    public GoogleTokenVerifierServiceImpl(@Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId) {
        this.clientId = clientId;
    }

    public GoogleIdToken.Payload verify(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                Utils.getDefaultTransport(), Utils.getDefaultJsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new RuntimeException("Invalid Google ID token");
        }
    }
}
