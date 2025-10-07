
package com.hcmut.ecommerce.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateGhtkOrderResponse {
    private boolean success;
    private Order order;
    private String message;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        private String partner_id;
        private String label;
        private String status;
    }
}

