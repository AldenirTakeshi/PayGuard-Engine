package com.payguard.core.infra.controller;

import com.payguard.core.domain.model.Transaction;
import com.payguard.core.domain.service.TransactionService;
import com.payguard.core.infra.controller.dto.CreateTransactionRequest;
import com.payguard.core.infra.controller.dto.TransactionDetailsResponse;
import com.payguard.core.infra.controller.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request){
        TransactionResponse response = transactionService.criarTransacao(request);

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{transactionId}")
    public  ResponseEntity<TransactionDetailsResponse> getTransactionById(@PathVariable UUID transactionId){
        try {
            TransactionDetailsResponse response = transactionService.findById(transactionId);
            return ResponseEntity.ok(response);
        } catch (Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
}
