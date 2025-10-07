package com.hcmut.ecommerce.domain.order.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeOptions {
    private int shipMoney;
    private String shipMoneyText;
}

