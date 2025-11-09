package com.hcmut.ecommerce.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstLoginInforRequest {
    private String tel;
    private String address;
    private String province;
    private String district;
    private String ward;
}
