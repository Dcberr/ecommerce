package com.hcmut.ecommerce.domain.order.dto.request;

import lombok.Data;

@Data
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

