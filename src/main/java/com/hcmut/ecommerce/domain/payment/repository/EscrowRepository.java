package com.hcmut.ecommerce.domain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.payment.model.Escrow;

public interface EscrowRepository extends JpaRepository<Escrow, String> {

}
