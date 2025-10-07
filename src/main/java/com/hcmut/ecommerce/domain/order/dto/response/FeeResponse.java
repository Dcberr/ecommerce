package com.hcmut.ecommerce.domain.order.dto.response;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeResponse {
    private String name;
    private int fee;
    private int insurance_fee;
    private int include_vat;
    private int cost_id;
    private String delivery_type;
    private int a;
    private String dt;
    private List<Object> extFees;
    private String promotion_key;
    private boolean delivery;
    private int ship_fee_only;
    private int distance;
    private long solutionFee;
    private FeeOptions options;
}

