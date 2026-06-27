package com.payguard.core.infra.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull(message = "A conta de origem é obrigatória")
        UUID accountOrigin,

        @NotNull(message = "A conta de destino é obrigatória")
        UUID accountDestination,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "A moeda é obrigatória")
        String currency
) {}