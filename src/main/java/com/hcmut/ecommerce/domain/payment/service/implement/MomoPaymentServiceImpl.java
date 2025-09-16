package com.hcmut.ecommerce.domain.payment.service.implement;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.checkerframework.checker.units.qual.N;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmut.ecommerce.domain.payment.dto.request.MomoCallbackRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.MomoPaymentRequest;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoCallbackResponse;
import com.hcmut.ecommerce.domain.payment.dto.response.MomoPaymentResponse;
import com.hcmut.ecommerce.domain.payment.service.interfaces.MomoPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MomoPaymentServiceImpl implements MomoPaymentService {

    // private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    // private final CustomerRepository customerRepository;
    // private final AuthService authService;

    @NonFinal
    @Value("${momo.access-key}")
    protected String ACCESS_KEY;

    @NonFinal
    @Value("${momo.secret-key}")
    protected String SECRET_KEY;

    @NonFinal
    @Value("${momo.partner-code}")
    protected String PARTNER_CODE;

    @NonFinal
    @Value("${momo.endpoint}")
    protected String END_POINT;

    @NonFinal
    @Value("${momo.ipn-url}")
    protected String IPN_URL;

    @NonFinal
    @Value("$momo.momo-redirect-url")
    protected String MOMO_REDIRECT_URL;

    // @NonFinal
    // @Value("${momo.queryEndpoint}")
    // protected String QUERY_ENDPOINT;

    public String generateHmacSHA256(Map<String, String> params, String secretKey) throws Exception {
        // Step 1: Construct the raw data string
        StringBuilder rawData = new StringBuilder();
        params.forEach((key, value) -> rawData.append(key).append("=").append(value).append("&"));

        // Remove the trailing "&"
        if (rawData.length() > 0) {
            rawData.setLength(rawData.length() - 1);
        }

        // Step 2: Generate HmacSHA256 hash
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hashBytes = mac.doFinal(rawData.toString().getBytes(StandardCharsets.UTF_8));

        // Step 3: Convert hash bytes to hexadecimal string
        StringBuilder hashHex = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hashHex.append('0');
            }
            hashHex.append(hex);
        }

        return hashHex.toString();
    }

    public MomoPaymentResponse createPaymentRequest(MomoPaymentRequest request) throws Exception {

        String orderId = UUID.randomUUID().toString().replaceAll("[^0-9]", "");
        // log.info(orderId);
        String requestType = "payWithMethod";
        String orderInfo = "Payment for " + orderId;

        Map<String, String> requestBody = new LinkedHashMap<>();
        requestBody.put("accessKey", ACCESS_KEY);
        requestBody.put("amount", request.getAmount());
        requestBody.put("extraData", "eyJza3VzIjoiIn0=");
        requestBody.put("ipnUrl", IPN_URL);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("partnerCode", PARTNER_CODE);
        requestBody.put("redirectUrl",
                MOMO_REDIRECT_URL);
        requestBody.put("requestId", orderId);
        requestBody.put("requestType", requestType);
        // requestBody.put("extraData",
        // Base64.getEncoder().encodeToString("{\"key\":\"123\"}".getBytes()));

        // Generate raw data string for signature
        String rawData = requestBody.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        // log.info(rawData);
        String signature = generateHmacSHA256(requestBody, SECRET_KEY);
        // String signature = generateHmacSHA256(requestBody, "at67qH6mk8w5Y1nAyMoYKMWACiEi2bsa");
        // log.info(signature);
        requestBody.put("signature", signature);
        requestBody.put("lang", "en");

        // Send POST request to MOMO endpoint
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(END_POINT, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            // log.info(jsonResponse.toString());

            MomoPaymentResponse momoResponse = objectMapper.treeToValue(jsonResponse, MomoPaymentResponse.class);
            // Handle fields
            String payUrl = jsonResponse.has("payUrl") ? jsonResponse.get("payUrl").asText() : null;
            String message = jsonResponse.has("message") ? jsonResponse.get("message").asText() : "No message provided";

            if (payUrl != null) {
                return momoResponse;
            } else {
                throw new RuntimeException("Pay URL is missing. Message: " + message);
            }
        } else {
            throw new RuntimeException("Failed to connect to MOMO API. HTTP Status: " + response.getStatusCode());
        }
    }

    public MomoCallbackResponse handleMomoCallback(MomoCallbackRequest callback) throws Exception{
        log.info("Received Momo callback: {}", callback);

        boolean isValid = verifySignature(callback);
        if (!isValid) {
            throw new RuntimeException("Signature verification failed");
        }

        if (callback.getResultCode() == 0) {
            log.info("Payment success for order {}", callback.getOrderId());
            return new MomoCallbackResponse("success", 0);
            // TODO: Update trạng thái đơn hàng trong DB
        } else {
            log.warn("Payment failed for order {}, message={}", callback.getOrderId(), callback.getMessage());
            // TODO: Update trạng thái thất bại
        }
        return new MomoCallbackResponse(callback.getMessage(), callback.getResultCode());
    }

    private boolean verifySignature(MomoCallbackRequest callback) {
        try {
            String rawData = "accessKey=" + ACCESS_KEY +
                    "&amount=" + callback.getAmount() +
                    "&extraData=" + callback.getExtraData() +
                    "&message=" + callback.getMessage() +
                    "&orderId=" + callback.getOrderId() +
                    "&orderInfo=" + callback.getOrderInfo() +
                    "&orderType=" + callback.getOrderType() +
                    "&partnerCode=" + callback.getPartnerCode() +
                    "&payType=" + callback.getPayType() +
                    "&requestId=" + callback.getRequestId() +
                    "&responseTime=" + callback.getResponseTime() +
                    "&resultCode=" + callback.getResultCode() +
                    "&transId=" + callback.getTransId();

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);

            byte[] hashBytes = sha256_HMAC.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

            // Convert sang hex lowercase
            StringBuilder hashHex = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashHex.append('0');
                hashHex.append(hex);
            }
            String signature = hashHex.toString();

            return signature.equals(callback.getSignature());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
