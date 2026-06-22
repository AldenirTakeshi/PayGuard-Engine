package com.payguard.core.domain.service;

import com.payguard.core.domain.model.LedgerEntry;
import com.payguard.core.domain.model.enums.TransactionType;
import com.payguard.core.domain.repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class LedgerService {

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerService(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    public BigDecimal obterSaldo(UUID accountId){
        return ledgerEntryRepository.calculateBalanceByAccountId(accountId);
    }

    public LedgerEntry criarLancamento(LedgerEntry lancamento) {
        if (lancamento.getType() == TransactionType.DEBIT) {
            BigDecimal saldoAtual = this.obterSaldo(lancamento.getAccountId());

            if (saldoAtual.compareTo(lancamento.getAmount()) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente para realizar esta transação.");
            }
        }

        return ledgerEntryRepository.save(lancamento);
    }
}
