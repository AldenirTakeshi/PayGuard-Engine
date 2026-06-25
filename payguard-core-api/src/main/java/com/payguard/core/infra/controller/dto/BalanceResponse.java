package com.payguard.core.infra.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponse(UUID accountId, BigDecimal balance) {
}
