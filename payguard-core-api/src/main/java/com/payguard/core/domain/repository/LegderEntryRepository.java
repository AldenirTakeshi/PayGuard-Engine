package com.payguard.core.domain.repository;

import com.payguard.core.domain.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LegderEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    Optional<LedgerEntry> findByAccountId(UUID accountId);
}
