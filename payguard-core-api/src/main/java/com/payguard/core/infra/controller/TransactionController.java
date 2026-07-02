package com.payguard.core.infra.controller;

import com.payguard.core.domain.model.Transaction;
import com.payguard.core.domain.service.TransactionService;
import com.payguard.core.infra.controller.dto.CreateTransactionRequest;
import com.payguard.core.infra.controller.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request){
        Transaction transaction = transactionService.registerTransacaoInicial(request);

        return ResponseEntity.accepted().body(new TransactionResponse(transaction.getId(), transaction.getStatus().name()));
    }
}
