package com.payguard.core.infra.config;

import com.payguard.core.domain.model.LedgerEntry;
import com.payguard.core.domain.model.enums.TransactionType;
import com.payguard.core.domain.repository.LedgerEntryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Configuration
@Profile({"dev", "local"})
public class DataBaseSeedConfig implements CommandLineRunner {

    private final LedgerEntryRepository ledgerEntryRepository;

    public DataBaseSeedConfig(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (ledgerEntryRepository.count() == 0) {
            System.out.println("Inicializando seed de dados para ambiente de desenvolvimento...");

            UUID contaCorrenteId = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");
            UUID contaPoupancaId = UUID.fromString("f6e5d4c3-b2a1-0f9e-8d7c-6b5a4f3e2d1c");

            LedgerEntry cargaInicial = new LedgerEntry();
            cargaInicial.setAccountId(contaCorrenteId);
            cargaInicial.setAmount(new BigDecimal("1500.00"));
            cargaInicial.setType(TransactionType.valueOf("CREDIT"));
            cargaInicial.setDescription("Carga inicial de teste");
            cargaInicial.setCreatedAt(LocalDateTime.now());

            LedgerEntry pagamentoAluguel = new LedgerEntry();
            pagamentoAluguel.setAccountId(contaCorrenteId);
            pagamentoAluguel.setAmount(new BigDecimal("600.00"));
            pagamentoAluguel.setType(TransactionType.valueOf("DEBIT"));
            pagamentoAluguel.setDescription("Pagamento de aluguel residencial");
            pagamentoAluguel.setCreatedAt(LocalDateTime.now().minusDays(1));

            LedgerEntry depositoPoupanca = new LedgerEntry();
            depositoPoupanca.setAccountId(contaPoupancaId);
            depositoPoupanca.setAmount(new BigDecimal("350.00"));
            depositoPoupanca.setType(TransactionType.valueOf("CREDIT"));
            depositoPoupanca.setDescription("Reserva mensal poupança");
            depositoPoupanca.setCreatedAt(LocalDateTime.now());

            ledgerEntryRepository.saveAll(List.of(cargaInicial, pagamentoAluguel, depositoPoupanca));

            System.out.println("Seed de dados finalizado com sucesso!");
        } else {
            System.out.println("Banco já possui dados. Pulando a etapa de seed.");
        }
    }
}
