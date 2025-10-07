package com.hcmut.ecommerce.domain.order.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcmut.ecommerce.domain.order.dto.request.CreateGhtkOrderRequest;
import com.hcmut.ecommerce.domain.order.dto.request.FeeRequest;
import com.hcmut.ecommerce.domain.order.dto.response.CreateGhtkOrderResponse;
import com.hcmut.ecommerce.domain.order.dto.response.FeeResponse;
import com.hcmut.ecommerce.domain.order.service.interfaces.DeliveryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DeliverySeviceImpl implements DeliveryService {
    @Value("${ghtk.api.base-url}")
    private String baseUrl;

    @Value("${ghtk.api.token}")
    private String apiToken;

    @Value("${ghtk.api.ghtk-partner-code}")
    private String ghtkPartnerCode;

    private HttpHeaders buildGhtkHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Client-Source", ghtkPartnerCode);
        headers.set("Token", apiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public CreateGhtkOrderResponse createOrder(CreateGhtkOrderRequest orderRequest) throws JsonMappingException, JsonProcessingException {
        String url = baseUrl + "/shipment/order";

        log.info(orderRequest.toString());

        HttpEntity<CreateGhtkOrderRequest> request = new HttpEntity<>(orderRequest, buildGhtkHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CreateGhtkOrderResponse> response = restTemplate.postForEntity(url, request, CreateGhtkOrderResponse.class);
        // log.info(response.toString());
        // log.info(response.getBody());

        // ObjectMapper mapper = new ObjectMapper();
        // JsonNode root = mapper.readTree(response.getBody());
        // JsonNode feeNode = root.path("order");

        // return mapper.treeToValue(feeNode, CreateGhtkOrderResponse.class);
        return response.getBody();
    }

    public String getOrder(String labelId) {
        String url = baseUrl + "/shipment/v2/" + labelId;

        HttpEntity<Void> request = new HttpEntity<>(buildGhtkHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }

    public String cancelOrder(String labelId) {
        String url = baseUrl + "/shipment/cancel/" + labelId;

        HttpEntity<Void> request = new HttpEntity<>(buildGhtkHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }

    public FeeResponse getShippingFee(FeeRequest req) throws JsonMappingException, JsonProcessingException {
        String url = String.format(
            baseUrl +
            "/shipment/fee?" +
            "pick_province=%s&pick_district=%s&province=%s&district=%s&address=%s&weight=%d&value=%d&transport=%s",
            req.getPickProvince(),
            req.getPickDistrict(),
            req.getProvince(),
            req.getDistrict(),
            req.getAddress(),
            req.getWeight(),
            req.getValue(),
            req.getTransport()
        );

        HttpEntity<Void> request = new HttpEntity<>(buildGhtkHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(url.toString(), HttpMethod.GET, request, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode feeNode = root.path("fee");

        return mapper.treeToValue(feeNode, FeeResponse.class);
    }

    public String trackOrder(String labelId) {
        String url = baseUrl + "/shipment/v2/" + labelId;

        HttpEntity<Void> request = new HttpEntity<>(buildGhtkHeaders());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return response.getBody();
    }
}
