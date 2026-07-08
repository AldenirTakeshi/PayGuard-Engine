package com.payguard.core.domain.service;

import com.payguard.core.domain.model.Transaction;
import com.payguard.core.domain.model.enums.TransactionStatus;
import com.payguard.core.domain.repository.TransactionRepository;
import com.payguard.core.infra.controller.dto.CreateTransactionRequest;
import com.payguard.core.infra.controller.dto.TransactionDetailsResponse;
import com.payguard.core.infra.controller.dto.TransactionResponse;
import com.payguard.core.infra.producer.TransactionEventPublisher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionEventPublisher eventPublisher;

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public TransactionDetailsResponse findById(UUID id){
        return transactionRepository.findById(id)
                .map(t -> new TransactionDetailsResponse(
                        t.getId(),
                        t.getAccountOriginId(),
                        t.getAccountDestinationId(),
                        t.getAmount(),
                        t.getCurrency(),
                        t.getStatus(),
                        t.getCreatedAt()
                ))
                .orElseThrow(() -> new EntityNotFoundException("Transação não encontrada para o ID fornecido."));
    }

    @Transactional
    public TransactionResponse criarTransacao(CreateTransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .accountOriginId(request.accountOrigin())
                .accountDestinationId(request.accountDestination())
                .amount(request.amount())
                .currency(request.currency())
                .status(TransactionStatus.PROCESSING)
                .createdAt(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        eventPublisher.publish(transaction);

        return new TransactionResponse(transaction.getId(), transaction.getStatus().toString());
    }
}
