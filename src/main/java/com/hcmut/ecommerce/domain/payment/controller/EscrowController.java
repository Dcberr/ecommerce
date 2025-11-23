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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/escrow")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Escrow", description = "Escrow management")
public class EscrowController {

    private final EscrowService escrowService;

    @Operation(summary = "List all escrows", description = "Retrieve all escrow records", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrows returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ApiResponse<List<Escrow>> getAllEscrows(){
        return ApiResponse.success(escrowService.getAllEscrows());
    }

    @Operation(summary = "Get escrow by id", description = "Retrieve an escrow by its id", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrow returned"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Escrow not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ApiResponse<Escrow> getEscrowById(@PathVariable String id){
        return ApiResponse.success(escrowService.getEscrowById(id));
    }

    @Operation(summary = "Create escrow", description = "Create a new escrow entry", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrow created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ApiResponse<Escrow> createEscrow(@RequestBody CreateEscrowRequest request){
        return ApiResponse.success(escrowService.createEscrow(request));
    }

    @Operation(summary = "Update escrow", description = "Update an existing escrow by id", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrow updated"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Escrow not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping("/{id}")
    public ApiResponse<Escrow> updateEscrow(@PathVariable String id, @RequestBody UpdateEscrowRequest request){
        return ApiResponse.success(escrowService.updateEscrow(id, request));
    }

    @Operation(summary = "Delete escrow", description = "Delete an escrow by id", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrow deleted"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Escrow not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEscrow(@PathVariable String id){
        escrowService.deleteEscrow(id);
        return ApiResponse.success(null);
    }

    @Operation(summary = "Release escrow", description = "Release funds from escrow according to provided request", tags = {"Escrow"})
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Escrow released"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/release")
    public ApiResponse<Void> releaseEscrow(@RequestBody ReleaseEscrowRequest request){
        escrowService.releaseEscrow(request);
        return ApiResponse.success(null);
    }

}
