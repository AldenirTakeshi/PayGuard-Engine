package com.payguard.core.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String EXCHANGE_NAME = "payguard.transactions";
    public static final String QUEUE_NAME = "payguard.transactions.v1.created";
    public static final String ROUTING_KEY = "transaction.created";

    @Bean
    public TopicExchange transactionExchange(){
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue transactionCreatedQueue(){
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(transactionCreatedQueue())
                .to(transactionExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
