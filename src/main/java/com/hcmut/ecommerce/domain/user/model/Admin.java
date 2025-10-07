package com.hcmut.ecommerce.domain.user.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADMIN")
@SuperBuilder
@Getter
@Setter
// @NoArgsConstructor        
// @AllArgsConstructor
public class Admin extends User { 


}

