package com.hcmut.ecommerce.domain.user.dto.request;

import com.hcmut.ecommerce.domain.user.model.User.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GoogleLoginRequest {
    private String code;
    private UserRole userRole;
}
