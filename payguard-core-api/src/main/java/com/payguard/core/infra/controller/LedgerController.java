package com.payguard.core.infra.controller;

import com.payguard.core.domain.service.LedgerService;
import com.payguard.core.infra.controller.dto.BalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable UUID accountId){
        BigDecimal balance = ledgerService.obterSaldo(accountId);

        if(balance == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new BalanceResponse(accountId, balance));
    };
}
