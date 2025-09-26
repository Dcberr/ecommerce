package com.hcmut.ecommerce.domain.payment.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hcmut.ecommerce.domain.order.model.Order;
import com.hcmut.ecommerce.domain.order.model.Order.OrderStatus;
import com.hcmut.ecommerce.domain.order.repository.OrderRepository;
import com.hcmut.ecommerce.domain.payment.dto.request.CreateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.ReleaseEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.UpdateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.payment.model.Escrow.EscrowStatus;
import com.hcmut.ecommerce.domain.payment.repository.EscrowRepository;
import com.hcmut.ecommerce.domain.payment.service.interfaces.EscrowService;
import com.hcmut.ecommerce.domain.user.model.User;
import com.hcmut.ecommerce.domain.user.repository.UserRepository;
import com.hcmut.ecommerce.domain.wallet.model.Wallet;
import com.hcmut.ecommerce.domain.wallet.repository.WalletRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EscrowServiceImpl implements EscrowService {

    private final EscrowRepository escrowRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;

    public List<Escrow> getAllEscrows() {
        return escrowRepository.findAll();
    }

    public Escrow getEscrowById(String id) {
        return escrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Escrow not found with id " + id));
    }

    public Escrow createEscrow(CreateEscrowRequest request) {
        LocalDateTime releaseAt = null;
        EscrowStatus escrowStatus = EscrowStatus.HOLDING;
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id " + request.getOrderId()));
        User buyer = userRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new RuntimeException("Buyer not found with id " + request.getBuyerId()));

        User seller = userRepository.findById(request.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found with id " + request.getSellerId()));

        Escrow escrow = Escrow.builder()
                .amount(request.getAmount())
                .buyer(buyer)
                .escrowStatus(escrowStatus)
                .seller(seller)
                .order(order)
                .createdAt(LocalDateTime.now())
                .releaseAt(releaseAt)
                .build();

        return escrowRepository.save(escrow);
    }

    public Escrow updateEscrow(String id, UpdateEscrowRequest request) {
        Escrow escrow = getEscrowById(id);
        escrow.setAmount(request.getAmount());
        escrow.setReleaseAt(request.getReleaseAt());
        return escrowRepository.save(escrow);
    }

    public void deleteEscrow(String id) {
        escrowRepository.deleteById(id);
    }

    public void releaseEscrow(ReleaseEscrowRequest request){
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id " + request.getOrderId()));

        order.setOrderStatus(OrderStatus.DELIVERED);

        Wallet wallet = walletRepository.findById(order.getSeller().getWallet().getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found with user id " + order.getSeller().getId()));
        
        wallet.setAmount(wallet.getAmount() + order.getEscrow().getAmount());

        walletRepository.save(wallet);

        order.getEscrow().setEscrowStatus(EscrowStatus.RELEASE);

        orderRepository.save(order);
    }
}
