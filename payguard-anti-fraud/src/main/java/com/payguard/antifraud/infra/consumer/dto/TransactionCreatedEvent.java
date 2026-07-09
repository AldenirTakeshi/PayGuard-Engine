package com.payguard.antifraud.infra.consumer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID eventId,
        UUID transactionId,
        String accountOrigin,
        String accountDestination,
        BigDecimal amount,
        String currency,
        LocalDateTime createdAt
) {
}
