package com.payguard.core.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID eventId,
        UUID transactionId,
        UUID accountOrigin,
        UUID accountDestination,
        BigDecimal amount,
        String currency,
        LocalDateTime createdAt
) {
}
