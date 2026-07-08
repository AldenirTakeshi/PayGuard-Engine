package com.payguard.core.infra.producer;

import com.payguard.core.domain.event.TransactionCreatedEvent;
import com.payguard.core.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionEventPublisher {

    private  final RabbitTemplate rabbitTemplate;

    public TransactionEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(com.payguard.core.domain.model.Transaction transaction){

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                UUID.randomUUID(),
                transaction.getId(),
                transaction.getAccountOriginId(),
                transaction.getAccountDestinationId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getCreatedAt()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                event
        );

        System.out.println("[Evento Publicado] Transaction ID: " + transaction.getId());
    }
}
