package com.payguard.core.infra.controller;

import com.payguard.core.infra.controller.dto.CreateTransactionRequest;
import com.payguard.core.infra.controller.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody CreateTransactionRequest request){

        UUID mockTransactionId = UUID.randomUUID();
        String status = "PROCESSING";

        return ResponseEntity.accepted().body(new TransactionResponse(mockTransactionId, status));
    }
}
