package com.payguard.core.domain.service;

import com.payguard.core.domain.model.Transaction;
import com.payguard.core.domain.model.enums.TransactionStatus;
import com.payguard.core.domain.repository.TransactionRepository;
import com.payguard.core.infra.controller.dto.CreateTransactionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction registerTransacaoInicial(CreateTransactionRequest request){
        Transaction transaction = Transaction.builder()
                .accountOriginId(request.accountOrigin())
                .accountDestinationId(request.accountDestination())
                .amount(request.amount())
                .currency(request.currency())
                .status(TransactionStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }
}
