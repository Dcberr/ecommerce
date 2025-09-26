package com.hcmut.ecommerce.domain.payment.service.interfaces;

import java.util.List;

import com.hcmut.ecommerce.domain.payment.dto.request.CreateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.ReleaseEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.UpdateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.model.Escrow;

public interface EscrowService {
    public List<Escrow> getAllEscrows();
    public Escrow getEscrowById(String id);
    public Escrow createEscrow(CreateEscrowRequest request);
    public Escrow updateEscrow(String id, UpdateEscrowRequest request);
    public void deleteEscrow(String id);
    public void releaseEscrow(ReleaseEscrowRequest request);
}
