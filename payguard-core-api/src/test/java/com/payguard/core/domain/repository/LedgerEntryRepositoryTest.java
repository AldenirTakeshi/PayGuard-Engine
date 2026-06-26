package com.payguard.core.domain.repository;

import com.payguard.core.domain.model.LedgerEntry;
import com.payguard.core.domain.model.enums.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LedgerEntryRepositoryTest {

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Test
    @DisplayName("Deve calcular o saldo somando créditos e subtraindo débitos corretamente")
    void deveCalcularSaldoSomandoCreditosESubtraindoDebitos() {
        UUID accountId = UUID.randomUUID();

        LedgerEntry credito1 = LedgerEntry.builder()
                .accountId(accountId)
                .type(TransactionType.CREDIT)
                .amount(new BigDecimal("150.0000"))
                .description("Pix recebido")
                .build();

        LedgerEntry debito = LedgerEntry.builder()
                .accountId(accountId)
                .type(TransactionType.DEBIT)
                .amount(new BigDecimal("40.0000"))
                .description("Pagamento boleto")
                .build();

        LedgerEntry credito2 = LedgerEntry.builder()
                .accountId(accountId)
                .type(TransactionType.CREDIT)
                .amount(new BigDecimal("10.5000"))
                .description("Estorno")
                .build();

        ledgerEntryRepository.save(credito1);
        ledgerEntryRepository.save(debito);
        ledgerEntryRepository.save(credito2);

        BigDecimal saldoCalculado = ledgerEntryRepository.calculateBalanceByAccountId(accountId);

        BigDecimal saldoEsperado = new BigDecimal("120.5000");
        assertEquals(0, saldoEsperado.compareTo(saldoCalculado), "O cálculo do saldo falhou!");
    }

    @Test
    @DisplayName("Deve retornar null quando a conta não possui nenhum lançamento (cenário do 404)")
    void deveRetornarNullQuandoContaNaoPossuiLancamentos() {
        UUID accountIdSemLancamentos = UUID.randomUUID();

        BigDecimal saldoCalculado = ledgerEntryRepository.calculateBalanceByAccountId(accountIdSemLancamentos);

        assertNull(saldoCalculado, "O saldo deveria ser nulo para uma conta inexistente!");
    }
}
