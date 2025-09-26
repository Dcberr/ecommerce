package com.hcmut.ecommerce.domain.payment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmut.ecommerce.common.response.ApiResponse;
import com.hcmut.ecommerce.domain.payment.dto.request.CreateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.ReleaseEscrowRequest;
import com.hcmut.ecommerce.domain.payment.dto.request.UpdateEscrowRequest;
import com.hcmut.ecommerce.domain.payment.model.Escrow;
import com.hcmut.ecommerce.domain.payment.service.interfaces.EscrowService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/escrow")
@RequiredArgsConstructor
public class EscrowController {

    private final EscrowService escrowService;

    @GetMapping
    public ApiResponse<List<Escrow>> getAllEscrows(){
        return ApiResponse.success(escrowService.getAllEscrows());
    }

    @GetMapping("/{id}")
    public ApiResponse<Escrow> getEscrowById(@PathVariable String id){
        return ApiResponse.success(escrowService.getEscrowById(id));
    }

    @PostMapping
    public ApiResponse<Escrow> createEscrow(@RequestBody CreateEscrowRequest request){
        return ApiResponse.success(escrowService.createEscrow(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<Escrow> updateEscrow(@PathVariable String id, @RequestBody UpdateEscrowRequest request){
        return ApiResponse.success(escrowService.updateEscrow(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEscrow(@PathVariable String id){
        escrowService.deleteEscrow(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/release")
    public ApiResponse<Void> releaseEscrow(@RequestBody ReleaseEscrowRequest request){
        escrowService.releaseEscrow(request);
        return ApiResponse.success(null);
    }

}
