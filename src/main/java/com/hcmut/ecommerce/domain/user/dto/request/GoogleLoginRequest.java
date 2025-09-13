package com.hcmut.ecommerce.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GoogleLoginRequest {
    private String code;
}
