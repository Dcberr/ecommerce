package com.hcmut.ecommerce.domain.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmut.ecommerce.domain.wallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, String>{

}
