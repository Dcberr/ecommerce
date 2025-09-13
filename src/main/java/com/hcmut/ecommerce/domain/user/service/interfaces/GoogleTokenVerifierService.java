package com.hcmut.ecommerce.domain.user.service.interfaces;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleTokenVerifierService {

    public GoogleIdToken.Payload verify(String idTokenString) throws Exception ;
}
