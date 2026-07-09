package com.payguard.antifraud.infra.consumer;

import com.payguard.antifraud.infra.consumer.dto.TransactionCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {

    @RabbitListener(queues = "payguard.transactions.v1.created")
    public void consume(TransactionCreatedEvent event, @Header("X-Correlation-ID") String correlationId){
        System.out.println("\n=================================================");
        System.out.println("🚨 [Antifraude] Evento Recebido com Sucesso!");
        System.out.println("🔗 [Correlation-ID]: " + correlationId);
        System.out.println("💰 ID da Transação: " + event.transactionId());
        System.out.println("💵 Valor: " + event.amount() + " " + event.currency());
        System.out.println("=================================================\n");
    }

}
