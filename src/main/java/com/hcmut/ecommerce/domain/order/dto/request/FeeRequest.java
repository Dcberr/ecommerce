package com.hcmut.ecommerce.domain.order.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@Builder
public class FeeRequest {
    private String pickProvince;
    private String pickDistrict;
    private String province;
    private String district;
    private String address;
    private int weight;   
    private int value;   
    private String transport; 
}

