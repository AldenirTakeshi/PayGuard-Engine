package com.payguard.core.domain.service;

import com.payguard.core.domain.model.LedgerEntry;
import com.payguard.core.domain.model.enums.TransactionType;
import com.payguard.core.domain.repository.LedgerEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LedgerServiceTest {

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    @InjectMocks
    private LedgerService ledgerService;

    @Test
    void deveRetornarOSaldoCorretoAoConsultar(){
        UUID accountId = UUID.randomUUID();

        BigDecimal saldoEsperado = new BigDecimal("250.7500");

        when(ledgerEntryRepository.calculateBalanceByAccountId(accountId)).thenReturn(saldoEsperado);

        BigDecimal saldoRetornado = ledgerService.obterSaldo(accountId);

        assertEquals(0, saldoEsperado.compareTo(saldoRetornado), "O Serviço não retornou o saldo correto repassado pelo repositório");

        verify(ledgerEntryRepository, times(1)).calculateBalanceByAccountId(accountId);
    }

    @Test
    void deveLancarExceptionQuandoDebitoExcederOSaldo() {
        UUID accountId = UUID.randomUUID();
        BigDecimal saldoAtual = new BigDecimal("50.0000");
        BigDecimal valorDebitoInsuficiente = new BigDecimal("60.0000");

        when(ledgerEntryRepository.calculateBalanceByAccountId(accountId)).thenReturn(saldoAtual);

        LedgerEntry lancamentoDebito = LedgerEntry.builder()
                .accountId(accountId)
                .type(TransactionType.DEBIT)
                .amount(valorDebitoInsuficiente)
                .description("Tentativa de saque ou compra")
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            ledgerService.criarLancamento(lancamentoDebito);
        });

        verify(ledgerEntryRepository, never()).save(any(LedgerEntry.class));
    }

    @Test
    void deveSalvarLancamentoComSucessoQuandoDadosForemValidos() {
        UUID accountId = UUID.randomUUID();

        LedgerEntry lancamentoCredito = LedgerEntry.builder()
                .accountId(accountId)
                .type(TransactionType.CREDIT)
                .amount(new BigDecimal("100.0000"))
                .description("Depósito recebido")
                .build();

        when(ledgerEntryRepository.save(any(LedgerEntry.class))).thenReturn(lancamentoCredito);

        LedgerEntry lancamentoSalvo = ledgerService.criarLancamento(lancamentoCredito);

        assertNotNull(lancamentoSalvo, "O lançamento retornado não deveria ser nulo!");
        assertEquals(accountId, lancamentoSalvo.getAccountId());
        assertEquals(TransactionType.CREDIT, lancamentoSalvo.getType());

        verify(ledgerEntryRepository, times(1)).save(lancamentoCredito);
    }
}
