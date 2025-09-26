package com.hcmut.ecommerce.domain.user.service.implement;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
import com.hcmut.ecommerce.domain.user.dto.response.AuthResponse;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.model.User.UserRole;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.user.service.interfaces.AuthService;
import com.hcmut.ecommerce.domain.user.service.interfaces.GoogleTokenVerifierService;
import com.hcmut.ecommerce.domain.wallet.model.Wallet;
import com.hcmut.ecommerce.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final GoogleTokenVerifierService googleVerifier;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Override
    public AuthResponse loginWithGoogle(GoogleLoginRequest request) throws Exception {
        log.info("Is running h");

        // log.info(request.toString());
        
        String id_token = exchangeCodeForIdToken(request);
        GoogleIdToken.Payload payload = googleVerifier.verify(id_token);
        // log.info(payload.toString());

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        log.info(email);
        log.info(picture);
        log.info(name);

        // Nếu user chưa tồn tại thì lưu vào DB
        User user = userRepository.findByEmail(email).orElseGet(() ->
                {
                    Wallet wallet = Wallet.builder()
                            .amount(0f)  // số dư ban đầu
                            .build();

                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .userRole(UserRole.BUYER)
                            .picture(picture)
                            .provider(User.AuthProvider.GOOGLE)
                            .wallet(wallet)
                            .build();

                    // thiết lập quan hệ 2 chiều
                    wallet.setUser(newUser);

                    return userRepository.save(newUser);
                }
        );

        

        // Sinh JWT cho hệ thống
        String jwt = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(jwt, email, name, picture);
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
        params.add("client_id", "257761733368-rs6t0vvag6rjielhh329o81t65t1lli4.apps.googleusercontent.com");
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", "http://localhost:3000");
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
}


