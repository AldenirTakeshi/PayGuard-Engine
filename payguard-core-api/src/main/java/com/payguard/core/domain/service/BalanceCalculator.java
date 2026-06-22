package com.payguard.core.domain.service;

import com.payguard.core.domain.model.LedgerEntry;
import com.payguard.core.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public class BalanceCalculator {

    public static BigDecimal calculate(List<LedgerEntry> entries){
        if(entries == null || entries.isEmpty()){
            return BigDecimal.ZERO.setScale(4);
        }

        BigDecimal balance = BigDecimal.ZERO;

        for(LedgerEntry entry : entries){
            if(entry.getType() == TransactionType.CREDIT){
                balance = balance.add(entry.getAmount());
            } else if (entry.getType() == TransactionType.DEBIT) {
                balance = balance.subtract(entry.getAmount());
            }
        }

        return balance.setScale(4);
    }
}
