package com.hcmut.ecommerce.domain.user.service.implement;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.hcmut.ecommerce.domain.user.dto.request.GoogleLoginRequest;
import com.hcmut.ecommerce.domain.user.dto.request.IntrospectRequest;
import com.hcmut.ecommerce.domain.user.dto.response.AuthResponse;
import com.hcmut.ecommerce.domain.user.dto.response.IntrospectResponse;
import com.hcmut.ecommerce.domain.user.model.Buyer;
import com.hcmut.ecommerce.domain.user.model.Seller;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.model.User.UserRole;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.user.service.interfaces.AuthService;
import com.hcmut.ecommerce.domain.user.service.interfaces.GoogleTokenVerifierService;
import com.hcmut.ecommerce.domain.user.service.interfaces.UserService;
import com.hcmut.ecommerce.domain.wallet.model.Wallet;
import com.hcmut.ecommerce.security.JwtUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.oauth2.sdk.ParseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final GoogleTokenVerifierService googleVerifier;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.google.expiration-time}")
    private String expirationTime;

    @Override
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) throws Exception {
        
        String id_token = exchangeCodeForIdToken(request);
        GoogleIdToken.Payload payload = googleVerifier.verify(id_token);
        // log.info(payload.toString());
        String jwt = null;

        if (request.getUserRole().equals(UserRole.SELLER)) {
            Seller seller = userService.sellerLogin(payload);
            jwt = generateToken(seller);
        } else if (request.getUserRole().equals(UserRole.ADMIN)) {
            
        } else {
            Buyer buyer = userService.buyerLogin(payload);
            jwt = generateToken(buyer);
        }

        return new AuthResponse(jwt);

    }

    @Override
    public String exchangeCodeForIdToken(GoogleLoginRequest request) {
        String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String rawCode = URLDecoder.decode(request.getCode(), StandardCharsets.UTF_8);

        // log.info("Exchange token with params: code={}, clientId={}, redirectUri={}", rawCode, clientId, redirectUri);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", rawCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> exchangeRequest = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, exchangeRequest, Map.class);
        // log.info(response.toString());

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // log.info((String) response.getBody().get("id_token"));
            return (String) response.getBody().get("id_token");
        }

        throw new RuntimeException("Failed to exchange code for id_token");
    }


    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("Dcberr")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus( Long.parseLong(expirationTime) , ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(clientSecret.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (Exception e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        UserRole role = user.getUserRole();
        if (role != null) {
            stringJoiner.add("ROLE_" + role.toString());
        }

        return stringJoiner.toString();
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {
        JWSVerifier jwsVerifier = new MACVerifier(clientSecret.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expriryTime = (isRefresh) ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                // .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        Boolean verified = signedJWT.verify(jwsVerifier);

        if (!(verified && expriryTime.after(new Date()))) {
            throw new Exception("Invalidated Token");
        }

        // if
        // (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
        // {
        // throw new Exception("Invalidated Token");
        // }

        // if (tokenBlacklistService.isTokenBlacklisted(token)) {
        //     throw new Exception("Invalidated Token!!!");
        // }

        return signedJWT;
    }
    
}


