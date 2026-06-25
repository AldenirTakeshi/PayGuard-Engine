package com.payguard.core.domain.repository;

import com.payguard.core.domain.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    Optional<LedgerEntry> findByAccountId(UUID accountId);
    @Query("""
        SELECT 
            SUM(CASE WHEN le.type = 'CREDIT' THEN le.amount ELSE -le.amount END)
        FROM LedgerEntry le
        WHERE le.accountId = :accountId
    """)
    BigDecimal calculateBalanceByAccountId(@Param("accountId") UUID accountId);
}
