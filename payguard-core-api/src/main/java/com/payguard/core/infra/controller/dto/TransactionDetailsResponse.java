package com.payguard.core.infra.controller.dto;

import com.payguard.core.domain.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDetailsResponse(
        UUID transactionId,
        UUID accountOrigin,
        UUID accountDestination,
        BigDecimal amount,
        String currency,
        TransactionStatus status,
        LocalDateTime createdAt
) {
}
